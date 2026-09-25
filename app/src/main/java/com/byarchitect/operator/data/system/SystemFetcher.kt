package com.byarchitect.operator.data.system

import com.byarchitect.operator.common.model.Error
import com.byarchitect.operator.common.model.Resource
import com.byarchitect.operator.R
import com.byarchitect.operator.data.model.ProcessLabel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SystemFetcher() {

    fun loadShell(): Flow<Resource<Any>> = flow {
        emit(Resource.Loading())
        when (val result = SystemAccess.connect()) {
            is SystemAccess.Result.Connected ->
                emit(Resource.Success(Any()))

            SystemAccess.Result.ShizukuPermissionRequired ->
                emit(Resource.Error(Error(R.string.error_shizuku_permission, null)))

            SystemAccess.Result.Unavailable ->
                emit(Resource.Error(Error(R.string.error_no_backend, null)))

            is SystemAccess.Result.Failed ->
                emit(Resource.Error(Error(R.string.error_shizuku_bind, result.cause as? Exception)))
        }
    }

    fun getProcessList(
        labels: List<ProcessLabel>,
        searchQuery: String
    ): Flow<Resource<List<Map<ProcessLabel, String>>>> = flow {
        emit(Resource.Loading())
        try {
            val labelsAsString = labels.joinToString(",") { it.label }
            val data = SystemAccess.requireRunner().exec("ps -A -o $labelsAsString").out.toMutableList()
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
            emit(Resource.Error(error = Error(messageResource = R.string.error_shell, exception = e)))
        }

    }

    fun killProcess(pid: Int, name: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        if (!SystemAccess.canKill(name)) {
            emit(Resource.Error(Error(R.string.cannot_kill_without_root, null)))
            return@flow
        }
        try {
            val result = SystemAccess.requireRunner().exec(SystemAccess.killCommand(pid, name))
            if (result.ok) {
                emit(Resource.Success(name))
            } else {
                emit(Resource.Error(Error(R.string.error_shell, null)))
            }
        } catch (e: Exception) {
            emit(Resource.Error(Error(messageResource = R.string.error_shell, exception = e)))
        }
    }
}
