package com.byarchitect.operator.data.system

import com.byarchitect.operator.common.model.Error
import com.byarchitect.operator.common.model.Resource
import com.byarchitect.operator.common.util.test
import com.byarchitect.operator.data.model.ProcessLabel
import com.byarchitect.operator.data.model.ProcessSortState
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SystemFetcher() {

    fun loadShell(): Flow<Resource<Any>> = flow {
        emit(Resource.Loading())
        ShellManager.closeShell()
        try {
            ShellManager.initializeShell()
        } catch (e: Exception) {
            emit(Resource.Error(error = Error(messageResource = com.byarchitect.operator.R.string.error_shell, exception = e)))
            return@flow
        }
        val shell = Shell.getShell()
        if (!shell.isAlive) {
            emit(
                Resource.Error(
                    Error(messageResource = com.byarchitect.operator.R.string.error_shell, null)
                )
            )
            return@flow
        }
        if (!shell.isRoot) {
            emit(
                Resource.Error(
                    Error(messageResource = com.byarchitect.operator.R.string.error_not_root, null)
                )
            )
            return@flow
        }
        emit(Resource.Success(Any()))

    }

    fun getProcessList(
        labels: List<ProcessLabel>,
        sortOrder: ProcessSortState,
        searchQuery: String
    ): Flow<Resource<List<Map<ProcessLabel, String>>>> = flow {
        emit(Resource.Loading())
        try {
            val labelsAsString = labels.joinToString(",") { it.label }
            val data = Shell.cmd("ps -A -o $labelsAsString ").exec().out.toMutableList()
            test = data
            val dataLines = data.drop(1)
            val processListMap: ArrayList<Map<ProcessLabel, String>> = ArrayList()
            if (searchQuery.isNotEmpty()) {
                for (line in dataLines) {
                    if (line.isBlank() || !line.contains(searchQuery, ignoreCase = true)) continue
                    val values = line.trim().replace("[", "").replace("]", "").split(Regex("\\s+"), labels.size)
                    val processMap = HashMap<ProcessLabel, String>()
                    for (i in labels.indices) {
                        processMap[labels[i]] = values[i]
                    }
                    processListMap.add(processMap)
                }
            } else {
                for (line in dataLines) {
                    if (line.isBlank()) continue
                    val values = line.trim().replace("[", "").replace("]", "").split(Regex("\\s+"), labels.size)
                    val processMap = HashMap<ProcessLabel, String>()
                    for (i in labels.indices) {
                        processMap[labels[i]] = values[i]
                    }
                    processListMap.add(processMap)
                }
            }
            /*
                        val processListMap: List<Map<ProcessLabel, String>> = dataLines
                            .filter { it.isNotBlank() }
                            .map { line ->
                                val values = line.trim().replace("[", "").replace("]", "").split(Regex("\\s+"), labels.size)
                                labels.zip(values).toMap()
                            }
            */
            processListMap
            emit(Resource.Success(processListMap))
        } catch (e: Exception) {
            emit(Resource.Error(error = Error(messageResource = com.byarchitect.operator.R.string.error_shell, exception = e)))
        }

    }

    fun killProcess(pid: Int): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val output = Shell.cmd("kill $pid").exec().out
            test = output
        } catch (e: Exception) {
            emit(Resource.Error(error = Error(messageResource = com.byarchitect.operator.R.string.error_shell, exception = e)))
        }
    }
}