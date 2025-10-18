package com.byarchitect.operator.data.repository

import com.byarchitect.operator.common.model.Error
import com.byarchitect.operator.common.model.Resource
import com.byarchitect.operator.data.model.ProcessSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProcessSettingsHandler {
    private val repository = ProcessSettingsRepository()
    fun getSettings(): Flow<Resource<ProcessSettings>> = flow {
        emit(Resource.Loading())
        try {
            val refreshRate = repository.getRefreshRate()
            emit(Resource.Success(ProcessSettings(refreshRate)))
        } catch (e: Exception) {
            emit(Resource.Error(error = Error(messageResource = com.byarchitect.operator.R.string.error_shell, exception = e)))
        }

    }

}