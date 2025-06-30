package com.byarchitect.operator.presentation.process.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byarchitect.operator.R
import com.byarchitect.operator.common.model.Error
import com.byarchitect.operator.common.model.Resource
import com.byarchitect.operator.data.model.ProcessLabel
import com.byarchitect.operator.data.model.ProcessListState
import com.byarchitect.operator.data.model.ProcessSettings
import com.byarchitect.operator.data.model.ProcessSortState
import com.byarchitect.operator.data.model.ShellState
import com.byarchitect.operator.data.model.TotalProcessUsage
import com.byarchitect.operator.data.repository.ProcessSettingsHandler
import com.byarchitect.operator.data.system.SystemFetcher
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class ProcessViewModel @Inject constructor(
    val systemFetcher: SystemFetcher
) : ViewModel() {

    private val settingsHandler = ProcessSettingsHandler()

    private val _processLabels = MutableStateFlow<List<ProcessLabel>>(ProcessSettings.default().processLabels)
    val processLabels: StateFlow<List<ProcessLabel>> = _processLabels.asStateFlow()

    private val _shellState = MutableStateFlow(ShellState())
    val shellState: StateFlow<ShellState> = _shellState.asStateFlow()

    private val _refreshInterval = MutableStateFlow(ProcessSettings.default().refreshRate)
    val refreshInterval: StateFlow<Long> = _refreshInterval.asStateFlow()

    private val _sortOrder = MutableStateFlow(ProcessSortState(ProcessLabel.CPU_PERCENTAGE, isAscending = false))
    val sortOrder: StateFlow<ProcessSortState> = _sortOrder.asStateFlow()

    private val _uiState = MutableStateFlow(ProcessListState())
    val uiState: StateFlow<ProcessListState> = _uiState.asStateFlow()


    val totalProcessUsageStack = mutableListOf<TotalProcessUsage>()

    private var refreshJob: Job? = null

    init {
        loadShell()
        applyFirstValuesFromSettingsRepository()
        setupListRefresh()
    }

    fun loadShell() {
        systemFetcher.loadShell().onEach { resource ->
            _shellState.value = when (resource) {
                is Resource.Loading -> ShellState(isLoading = true)
                is Resource.Success -> ShellState(isLoading = false, success = true)
                is Resource.Error -> ShellState(isLoading = false, error = resource.error)
            }
        }.launchIn(viewModelScope)
    }

    private fun setupListRefresh() {

        combine(shellState, refreshInterval, processLabels, sortOrder) { shellState, refreshInterval, processLabels, sortOrder ->
            if (shellState.error != null) {
                pauseRefresh()
            } else {
                handleRefreshConfigChange(processLabels, refreshInterval, sortOrder)
            }
        }.launchIn(viewModelScope)
    }

    fun applyFirstValuesFromSettingsRepository() {
        settingsHandler.getSettings().onEach { resource ->
            val defaultValues = ProcessSettings.default()
            when (resource) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    _processLabels.value = resource.data?.processLabels ?: defaultValues.processLabels
                    _refreshInterval.value = resource.data?.refreshRate ?: defaultValues.refreshRate
                }

                else -> {
                    _processLabels.value = defaultValues.processLabels
                    _refreshInterval.value = defaultValues.refreshRate
                }

            }
        }.launchIn(viewModelScope)
    }


    private suspend fun refreshProcessList(processLabelList: List<ProcessLabel>, sortOrder: ProcessSortState) {
        systemFetcher.getProcessList(processLabelList)
            .onEach { resource ->
                _uiState.value = when (resource) {
                    is Resource.Loading -> _uiState.value.copy(isLoading = true)
                    is Resource.Success -> {
                        val processes: List<Map<ProcessLabel, String>> = resource.data ?: emptyList();
                        val sortedProcesses = if (sortOrder.isAscending) processes.sortedBy { row ->
                            row[sortOrder.label]?.toFloatOrNull() ?: 0f
                        } else processes.sortedByDescending { row ->
                            row[sortOrder.label]?.toFloatOrNull() ?: 0f
                        }

                        _uiState.value.copy(
                            isLoading = false,
                            processes = sortedProcesses,
                            error = null
                        )
                    }

                    is Resource.Error -> _uiState.value.copy(
                        isLoading = false,
                        error = resource.error
                    )
                }
            }
            .catch { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = Error(messageResource = R.string.refresh_error, exception = Exception(e))
                )
            }.collect()
    }

    private suspend fun refreshTotalProcessUsage() {
        systemFetcher.getTotalProcessUsage().onEach { resource ->
            if (resource is Resource.Success && resource.data != null) {
                totalProcessUsageStack.add(resource.data)
            }
        }.collect()
    }

    private fun startPeriodicRefresh(processLabelList: List<ProcessLabel>, intervalMilliseconds: Long, sortOrder: ProcessSortState) {
        refreshJob = viewModelScope.launch {
            while (isActive) {
                refreshProcessList(processLabelList, sortOrder)
                refreshTotalProcessUsage()
                delay(intervalMilliseconds)
            }
        }
    }

    private fun handleRefreshConfigChange(processLabelList: List<ProcessLabel>, refreshInterval: Long, sortOrder: ProcessSortState) {
        refreshJob?.cancel()

        startPeriodicRefresh(processLabelList, refreshInterval, sortOrder)
    }


    fun resumeRefresh() {
        if (refreshJob != null)
            refreshJob?.start()
        else
            startPeriodicRefresh(_processLabels.value, _refreshInterval.value, _sortOrder.value)
    }

    fun pauseRefresh() {
        refreshJob?.cancel()
    }


    fun setRefreshInterval(milliSeconds: Long) {
        _refreshInterval.value = if (milliSeconds > 0) milliSeconds else ProcessSettings.default().refreshRate
    }

    fun sortProcess(processLabel: ProcessLabel) {
        val sortOrder = _sortOrder.value
        if (sortOrder.label == processLabel) {
            _sortOrder.value = ProcessSortState(processLabel, !sortOrder.isAscending)
        } else {
            _sortOrder.value = sortOrder.copy(label = processLabel)
        }
    }

    fun updateProcessLabels(labels: List<ProcessLabel>) {
        _processLabels.value = labels
    }

    fun killProcess(pid: Int) {
        systemFetcher.killProcess(pid).launchIn(viewModelScope)
    }


}