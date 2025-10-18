package com.byarchitect.operator.data.system

import com.byarchitect.operator.common.model.Resource
import com.byarchitect.operator.data.model.Process
import com.byarchitect.operator.data.model.ProcessLabel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProcessManager {

    private val systemFetcher = SystemFetcher()

    fun getMainProcesses(searchQuery: String): Flow<Resource<List<Process>>> {
        return systemFetcher.getProcessList(
            labels = ProcessLabel.default(),
            searchQuery = searchQuery
        ).map { resource ->
            when (resource) {
                is Resource.Loading -> Resource.Loading()
                is Resource.Error -> Resource.Error(resource.error ?: com.byarchitect.operator.common.model.Error.unknownError())
                is Resource.Success -> {
                    val processes = resource.data?.mapNotNull { processMap ->
                        try {
                            Process(
                                pid = processMap[ProcessLabel.PID]?.toIntOrNull() ?: 0,
                                packageName = processMap[ProcessLabel.NAME] ?: "",
                                cpuPercentage = processMap[ProcessLabel.CPU_PERCENTAGE]?.toFloatOrNull() ?: 0f,
                                memPercentage = processMap[ProcessLabel.MEM_PERCENTAGE]?.toFloatOrNull() ?: 0f
                            )
                        } catch (e: Exception) {
                            null // Skip invalid process entries
                        }
                    } ?: emptyList()
                    Resource.Success(processes)
                }
            }
        }
    }
}