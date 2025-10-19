package com.byarchitect.operator.presentation.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.byarchitect.operator.common.model.Resource
import com.byarchitect.operator.data.model.ProcessSettings
import com.byarchitect.operator.data.repository.SettingsHandler
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach

data class SettingsViewModel @Inject constructor(
    val settingsHandler: SettingsHandler
) : ViewModel() {


    private val _processSettings = MutableStateFlow<ProcessSettings>(ProcessSettings.default())
    val processSettings: StateFlow<ProcessSettings> = _processSettings

    init {
        settingsHandler.getProcessSettings().onEach {
            if (it is Resource.Success) {
                _processSettings.value = it.data!!
            }
        }
    }

    fun setRefreshInterval(milliSecondsData: String?) {
        if (milliSecondsData == null) return

        val milliSeconds =
            if (milliSecondsData.isEmpty() || milliSecondsData.isBlank() || milliSecondsData.toLong() < 0L) 0L else milliSecondsData.toLong()
        milliSeconds.let {
            val processSettings = _processSettings.value.copy(refreshRate = milliSeconds.toLong())
            _processSettings.value = processSettings
            settingsHandler.setProcessSettings(processSettings)

        }
    }
}
