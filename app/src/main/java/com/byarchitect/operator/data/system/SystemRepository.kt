package com.byarchitect.operator.data.system

import com.byarchitect.operator.data.model.ProcessLabel
import com.topjohnwu.superuser.Shell

class SystemRepository {
    fun getProcessList(labels: List<ProcessLabel>): List<Map<String, String>> {

        val processListMap = mutableListOf<Map<String, String>>()

        Shell.setDefaultBuilder(
            Shell.Builder.create().setFlags(Shell.FLAG_REDIRECT_STDERR).setTimeout(100)
        )

        if (!Shell.getShell().isRoot) {
            throw RuntimeException("Root access not available")
        }

        val labelsAsString = labels.joinToString(",") { it.label }

        val pidDirs = Shell.cmd("ps -A -o $labelsAsString | awk '{print \$1\",\"\$2}' ").exec().out.toMutableList()


        pidDirs.removeAt(0)

        pidDirs.forEachIndexed { index, line ->
            val tokens = line.split(",")

            val tokenMap = mutableMapOf<String, String>()


            tokens.forEachIndexed { indexOfToken, token ->

                tokenMap[labels[indexOfToken].label] = token

            }

            processListMap.add(tokenMap)

        }

        return processListMap
    }
}