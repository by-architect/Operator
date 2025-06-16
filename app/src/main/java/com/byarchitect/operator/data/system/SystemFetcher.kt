package com.byarchitect.operator.data.system

import android.util.Log
import com.byarchitect.operator.common.model.Resource
import com.byarchitect.operator.data.model.ProcessLabel
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SystemFetcher {
    fun getProcessList(labels: List<ProcessLabel>): Flow<Resource<List<Map<ProcessLabel, String>>>> = flow {
        emit(Resource.Loading())

        try {
            Shell.setDefaultBuilder(
                Shell.Builder.create().setFlags(Shell.FLAG_REDIRECT_STDERR).setTimeout(100)
            )
        } catch (e: Exception) {
            Log.e("error", e.message.toString())
        }

        if (!Shell.getShell().isRoot) {
            emit(
                Resource.Error(
                    messageId = com.byarchitect.operator.R.string.error_not_root, null
                )
            )
        }

        val labelsAsString = labels.joinToString(",") { it.label }

        val pidDirs = Shell.cmd("ps -A -o $labelsAsString ").exec().out.toMutableList()


        val dataLines = pidDirs.drop(1)

        val processListMap: List<Map<ProcessLabel, String>> = dataLines
            .filter { it.isNotBlank() }
            .map { line ->
                val values = line.trim().split(Regex("\\s+"), labels.size)
                labels.zip(values).toMap()
            }

        emit(Resource.Success(processListMap))

    }
}