package com.byarchitect.operator.data.system

import com.byarchitect.operator.IUserService
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.system.exitProcess

/**
 * Runs inside a process owned by the ADB shell user, launched by Shizuku.
 *
 * Shizuku no longer exposes a way to spawn a process directly, so commands are executed
 * here instead. The shell user can read the whole process table and force-stop packages,
 * which is what Operator needs when root is unavailable.
 */
class ShizukuUserService : IUserService.Stub() {

    override fun destroy() {
        exitProcess(0)
    }

    override fun execute(command: String): Array<String> {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
            val lines = BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                reader.readLines()
            }
            val code = process.waitFor()
            (listOf(code.toString()) + lines).toTypedArray()
        } catch (t: Throwable) {
            arrayOf("1")
        }
    }
}
