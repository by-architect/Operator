package com.byarchitect.operator.data.system

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import com.byarchitect.operator.IUserService
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import rikka.shizuku.Shizuku
import kotlin.coroutines.resume

/**
 * Decides how the app reaches the system, and holds the active [CommandRunner].
 *
 * Root is preferred because it can do everything. Where there is no root, Shizuku runs
 * commands as the ADB shell user, which is enough to read the process table and to
 * force-stop packages.
 */
object SystemAccess {

    const val SHIZUKU_PERMISSION_REQUEST = 4231

    private const val BIND_TIMEOUT_MS = 10_000L

    private var appContext: Context? = null

    @Volatile
    private var runner: CommandRunner? = null

    /** Which backend is currently in use. */
    val backend: Backend get() = runner?.backend ?: Backend.NONE

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    /** Why a connection attempt did not produce a usable backend. */
    sealed interface Result {
        data class Connected(val backend: Backend) : Result
        /** Shizuku is running but has not granted us permission yet. */
        data object ShizukuPermissionRequired : Result
        /** Neither root nor Shizuku is available. */
        data object Unavailable : Result
        data class Failed(val cause: Throwable?) : Result
    }

    /**
     * Picks a backend: root first, Shizuku second.
     *
     * Root failing is not an error on its own - a device without root is expected to fall
     * through to Shizuku, and only the combination of both being unavailable is a problem.
     */
    suspend fun connect(): Result {
        runner = null

        if (tryRoot()) {
            runner = RootCommandRunner
            return Result.Connected(Backend.ROOT)
        }

        if (!isShizukuRunning()) return Result.Unavailable

        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            return Result.ShizukuPermissionRequired
        }

        return try {
            val service = withTimeoutOrNull(BIND_TIMEOUT_MS) { bindShizuku() }
                ?: return Result.Failed(null)
            runner = ShizukuCommandRunner(service)
            Result.Connected(Backend.SHIZUKU)
        } catch (t: Throwable) {
            Result.Failed(t)
        }
    }

    fun requireRunner(): CommandRunner =
        runner ?: throw IllegalStateException("No backend connected")

    /**
     * How to stop a process on the active backend.
     *
     * Root can signal anything. The Shizuku shell user cannot signal a process owned by
     * another app, so packages are stopped through the activity manager instead. Native
     * processes have no package to stop and stay out of reach without root.
     */
    fun killCommand(pid: Int, name: String): String = when (backend) {
        Backend.SHIZUKU -> if (looksLikePackage(name)) "am force-stop $name" else "kill $pid"
        else -> "kill $pid"
    }

    /** True when the given process can actually be stopped on the active backend. */
    fun canKill(name: String): Boolean =
        backend != Backend.SHIZUKU || looksLikePackage(name)

    private fun looksLikePackage(name: String): Boolean =
        name.contains('.') && name.none { it.isWhitespace() || it == '/' }

    fun isShizukuInstalled(): Boolean {
        val pm = appContext?.packageManager ?: return false
        return runCatching {
            @Suppress("DEPRECATION")
            pm.getPackageInfo("moe.shizuku.privileged.api", 0)
            true
        }.getOrDefault(false)
    }

    fun isShizukuRunning(): Boolean = runCatching { Shizuku.pingBinder() }.getOrDefault(false)

    fun requestShizukuPermission() {
        runCatching { Shizuku.requestPermission(SHIZUKU_PERMISSION_REQUEST) }
    }

    /** Handle for removing a permission listener again. */
    fun interface ListenerHandle {
        fun remove()
    }

    /**
     * Notifies when the user answers the Shizuku permission dialog. Kept here so the UI
     * layer does not have to depend on Shizuku directly.
     */
    fun addPermissionResultListener(onResult: (granted: Boolean) -> Unit): ListenerHandle {
        val listener = Shizuku.OnRequestPermissionResultListener { _, grantResult ->
            onResult(grantResult == PackageManager.PERMISSION_GRANTED)
        }
        runCatching { Shizuku.addRequestPermissionResultListener(listener) }
        return ListenerHandle {
            runCatching { Shizuku.removeRequestPermissionResultListener(listener) }
        }
    }

    private fun tryRoot(): Boolean = runCatching {
        ShellManager.closeShell()
        ShellManager.initializeShell()
        val shell = Shell.getShell()
        shell.isAlive && shell.isRoot
    }.getOrDefault(false)

    private suspend fun bindShizuku(): IUserService? = suspendCancellableCoroutine { cont ->
        val context = appContext
        if (context == null) {
            cont.resume(null)
            return@suspendCancellableCoroutine
        }

        val args = Shizuku.UserServiceArgs(
            ComponentName(context.packageName, ShizukuUserService::class.java.name)
        )
            .daemon(false)
            .processNameSuffix("shizuku")
            .debuggable(false)
            .version(1)

        val connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                val service = binder
                    ?.takeIf { it.pingBinder() }
                    ?.let { IUserService.Stub.asInterface(it) }
                if (cont.isActive) cont.resume(service)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                // The shell process went away; fall back to "no backend" so the UI can retry.
                if (runner?.backend == Backend.SHIZUKU) runner = null
            }
        }

        runCatching { Shizuku.bindUserService(args, connection) }
            .onFailure { if (cont.isActive) cont.resume(null) }
    }
}
