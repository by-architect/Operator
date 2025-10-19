package com.byarchitect.operator.presentation.process.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byarchitect.operator.R
import com.byarchitect.operator.common.model.Error
import com.byarchitect.operator.common.model.Resource
import com.byarchitect.operator.data.model.Process
import com.byarchitect.operator.data.model.ProcessLabel
import com.byarchitect.operator.data.model.ProcessSettings
import com.byarchitect.operator.data.model.ProcessSortState
import com.byarchitect.operator.data.model.ProcessState
import com.byarchitect.operator.data.model.ShellState
import com.byarchitect.operator.data.repository.SettingsHandler
import com.byarchitect.operator.data.system.ProcessManager
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
    val systemFetcher: SystemFetcher,
    val processManager: ProcessManager,
    private val settingsHandler: SettingsHandler
) : ViewModel() {

    private val _processLabels = MutableStateFlow<List<ProcessLabel>>(ProcessLabel.default())
    val processLabels: StateFlow<List<ProcessLabel>> = _processLabels.asStateFlow()

    private val _shellState = MutableStateFlow(ShellState())
    val shellState: StateFlow<ShellState> = _shellState.asStateFlow()

    private val _refreshInterval = MutableStateFlow(ProcessSettings.default().refreshRate)
    val refreshInterval: StateFlow<Long> = _refreshInterval.asStateFlow()

    private val _sortOrder = MutableStateFlow(ProcessSortState(ProcessLabel.CPU_PERCENTAGE, isAscending = false))
    val sortOrder: StateFlow<ProcessSortState> = _sortOrder.asStateFlow()

    private val _processState = MutableStateFlow(ProcessState())
    val processState: StateFlow<ProcessState> = _processState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedProcess = MutableStateFlow<Process?>(null)
    val selectedProcess: StateFlow<Process?> = _selectedProcess.asStateFlow()

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

        combine(
            shellState,
            refreshInterval,
            processLabels,
            sortOrder,
            searchQuery
        ) { shellState, refreshInterval, processLabels, sortOrder, searchQuery ->
            if (shellState.error != null) {
                pauseRefresh()
            } else {
                handleRefreshConfigChange(processLabels, refreshInterval, sortOrder, searchQuery)
            }
        }.launchIn(viewModelScope)
    }

    fun applyFirstValuesFromSettingsRepository() {
        settingsHandler.getProcessSettings().onEach { resource ->
            val defaultValues = ProcessSettings.default()
            when (resource) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    _refreshInterval.value = resource.data?.refreshRate ?: defaultValues.refreshRate
                }

                else -> {
                    _refreshInterval.value = defaultValues.refreshRate
                }

            }
        }.launchIn(viewModelScope)
    }


    private suspend fun refreshProcessList(sortOrder: ProcessSortState, searchQuery: String) {
        processManager.getMainProcesses(searchQuery)
            .onEach { resource ->
                _processState.value = when (resource) {
                    is Resource.Loading -> _processState.value.copy(isLoading = true)
                    is Resource.Success -> {
                        val processes: List<Process> = resource.data ?: emptyList()
                        val sortedProcesses = if (sortOrder.isAscending)
                            when (sortOrder.label) {
                                ProcessLabel.CPU_PERCENTAGE -> processes.sortedBy { it.cpuPercentage.toFloat() }
                                ProcessLabel.MEM_PERCENTAGE -> processes.sortedBy { it.memPercentage.toFloat() }
                                ProcessLabel.PID -> processes.sortedBy { it.pid.toFloat() }
                                ProcessLabel.NAME -> processes.sortedBy { it.packageName }
                                else -> processes
                            }
                        else
                            when (sortOrder.label) {
                                ProcessLabel.CPU_PERCENTAGE -> processes.sortedByDescending { it.cpuPercentage.toFloat() }
                                ProcessLabel.MEM_PERCENTAGE -> processes.sortedByDescending { it.memPercentage.toFloat() }
                                ProcessLabel.PID -> processes.sortedByDescending { it.pid.toFloat() }
                                ProcessLabel.NAME -> processes.sortedByDescending { it.packageName }
                                else -> processes
                            }
                        _processState.value.copy(
                            isLoading = false,
                            processes = sortedProcesses,
                            error = null
                        )
                    }

                    is Resource.Error -> _processState.value.copy(
                        isLoading = false,
                        error = resource.error
                    )
                }
            }
            .catch { e ->
                _processState.value = _processState.value.copy(
                    isLoading = false,
                    error = Error(messageResource = R.string.refresh_error, exception = Exception(e))
                )
            }.collect()
    }

    private fun startPeriodicRefresh(
        intervalMilliseconds: Long,
        sortOrder: ProcessSortState,
        searchQuery: String
    ) {
        refreshJob = viewModelScope.launch {
            while (isActive) {
                refreshProcessList( sortOrder, searchQuery)
                delay(intervalMilliseconds)
            }
        }
    }

    private fun handleRefreshConfigChange(
        processLabelList: List<ProcessLabel>,
        refreshInterval: Long,
        sortOrder: ProcessSortState,
        searchQuery: String
    ) {
        refreshJob?.cancel()

        startPeriodicRefresh( refreshInterval, sortOrder, searchQuery)
    }


    fun resumeRefresh() {
        if (refreshJob != null)
            refreshJob?.start()
        else
            startPeriodicRefresh(_refreshInterval.value, _sortOrder.value, searchQuery.value)
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

    fun searchProcess(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun updateProcessLabels(labels: List<ProcessLabel>) {
        _processLabels.value = labels
    }

    fun killProcess(pid: Int) {
        systemFetcher.killProcess(pid).launchIn(viewModelScope)
    }

    fun selectProcess(process: Process) {
        _selectedProcess.value = process
    }

    fun deselectProcess() {
        _selectedProcess.value = null
    }

}