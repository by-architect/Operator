package com.byarchitect.operator.data.system

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread

class Terminal {

    fun runCmd(
        cmd: List<String>,
        stdoutList: MutableList<String>? = null,
        stderrList: MutableList<String>? = null,
        redirectStderr: Boolean = false
    ): Int {
        val processBuilder = ProcessBuilder(cmd)
            .redirectErrorStream(redirectStderr)
        var process: Process? = null
        try {
            process = processBuilder.start()
            val stderrStream = process.errorStream
            val stdoutStream = process.inputStream

            val stdoutThread = thread {
                try {
                    BufferedReader(InputStreamReader(stdoutStream)).use {
                        while (true) {
                            val line = it.readLine() ?: break
                            stdoutList?.add(line)
                        }
                    }
                } catch (_: Exception) {
                }
            }
            val stderrThread = thread {
                try {
                    BufferedReader(InputStreamReader(stderrStream)).use {
                        while (true) {
                            val line = it.readLine() ?: break
                            stderrList?.add(line)
                        }
                    }
                } catch (_: Exception) {
                }
            }

            val exitCode = process.waitFor()

            try {
                stderrThread.join(1000)
            } catch (_: InterruptedException) {
            }

            try {
                stdoutThread.join(1000)
            } catch (_: InterruptedException) {
            }

            return exitCode
        } catch (_: Exception) {
            return -1
        } finally {
            process?.destroy()
        }
    }

    fun execute(command: String) {

        Log.e("Terminal", commandExecuter(command))

    }

    private fun commandExecuter(command: String): String {
        try {
            val process = Runtime.getRuntime().exec("su") // Switch to root
            val outputStream = process.outputStream
            val inputStream = process.inputStream
            val errorStream = process.errorStream

            outputStream.write(command.toByteArray())
            outputStream.flush()
            outputStream.write("exit\n".toByteArray())
            outputStream.flush()

            val output = inputStream.bufferedReader().readText()
            val error = errorStream.bufferedReader().readText()
            process.waitFor()

            return if (output.isNotEmpty()) output else error
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

}