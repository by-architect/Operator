package com.byarchitect.operator.data.system

import com.byarchitect.operator.IUserService
import com.topjohnwu.superuser.Shell

/** How the app is reaching the system. */
enum class Backend { ROOT, SHIZUKU, NONE }

/** Result of a shell command: its stdout lines and its exit code. */
data class CmdResult(val out: List<String>, val code: Int) {
    val ok: Boolean get() = code == 0
}

/**
 * Somewhere commands can be run with enough privilege to read the process table.
 *
 * Root can do everything. Shizuku runs as the ADB shell user, which can list every
 * process but cannot signal one owned by another app - see [SystemAccess.killCommand].
 */
interface CommandRunner {
    val backend: Backend
    fun exec(command: String): CmdResult
}

/** Commands through a root shell, via libsu. */
object RootCommandRunner : CommandRunner {
    override val backend = Backend.ROOT

    override fun exec(command: String): CmdResult {
        val result = Shell.cmd(command).exec()
        return CmdResult(result.out, if (result.isSuccess) 0 else 1)
    }
}

/** Commands through a Shizuku user service, running as the ADB shell user. */
class ShizukuCommandRunner(private val service: IUserService) : CommandRunner {
    override val backend = Backend.SHIZUKU

    override fun exec(command: String): CmdResult {
        val raw = service.execute(command) ?: return CmdResult(emptyList(), 1)
        if (raw.isEmpty()) return CmdResult(emptyList(), 1)
        val code = raw.first().toIntOrNull() ?: 1
        return CmdResult(raw.drop(1), code)
    }
}
