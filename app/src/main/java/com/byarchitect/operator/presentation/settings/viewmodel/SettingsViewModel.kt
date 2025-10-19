package com.byarchitect.operator.presentation.settings.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byarchitect.operator.R
import com.byarchitect.operator.common.model.Resource
import com.byarchitect.operator.data.model.ProcessSettings
import com.byarchitect.operator.data.repository.SettingsHandler
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

data class SettingsViewModel @Inject constructor(
    val settingsHandler: SettingsHandler
) : ViewModel() {


    private val _refreshInterval = MutableStateFlow(ProcessSettings.default().refreshRateAsSeconds.toString())
    val refreshInterval: StateFlow<String> = _refreshInterval

    private val _processSettings = MutableStateFlow(ProcessSettings.default())
    val processSettings: StateFlow<ProcessSettings> = _processSettings

    init {
        settingsHandler.getProcessSettings().onEach {
            if (it is Resource.Success) {
                _processSettings.value = it.data!!
                _refreshInterval.value = it.data.refreshRateAsSeconds.toString()
            }
        }.launchIn(viewModelScope)
    }

    fun setRefreshInterval(milliSecondsData: String?) {
        _refreshInterval.value = milliSecondsData ?: ""
    }

    fun saveIntervalToDatabase(context: Context) {
        if (_refreshInterval.value.isEmpty()) {
            return
        }
        val refreshRateInMillis = _refreshInterval.value.toLongOrNull()?.times(1000) ?: 3000L
        val processSettings = _processSettings.value.copy(refreshRate = refreshRateInMillis)
        _processSettings.value = processSettings

        settingsHandler.setProcessSettings(processSettings).onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    Toast.makeText(context, R.string.interval_saved, Toast.LENGTH_SHORT).show()
                }

                is Resource.Error -> {
                    Toast.makeText(context, R.string.interval_saved_error, Toast.LENGTH_SHORT).show()

                }

                is Resource.Loading -> {
                }
            }
        }.launchIn(viewModelScope)
    }
}
