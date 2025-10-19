package com.byarchitect.operator.data.repository

import android.content.Context
import com.byarchitect.operator.common.model.Error
import com.byarchitect.operator.common.model.Resource
import com.byarchitect.operator.data.model.ProcessSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SettingsHandler(context: Context) {
    private val repository = SettingsRepository(context)

    fun getProcessSettings(): Flow<Resource<ProcessSettings>> = flow {
        emit(Resource.Loading())
        try {
            val refreshRate = repository.getRefreshRate()
            emit(Resource.Success(ProcessSettings(refreshRate)))
        } catch (e: Exception) {
            emit(Resource.Error(error = Error(messageResource = com.byarchitect.operator.R.string.error_shell, exception = e)))
        }
    }
    fun setProcessSettings(processSettings: ProcessSettings): Flow<Resource<Any>> = flow {
        emit(Resource.Loading())
        try {
            repository.setRefreshRate(processSettings.refreshRate)
            emit(Resource.Success(Any()))
        } catch (e: Exception) {
            emit(Resource.Error(error = Error(messageResource = com.byarchitect.operator.R.string.error_shell, exception = e)))
        }
    }
}