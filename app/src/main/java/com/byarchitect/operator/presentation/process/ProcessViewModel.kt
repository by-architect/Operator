package com.byarchitect.operator.presentation.process

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byarchitect.operator.R
import com.byarchitect.operator.common.model.Resource
import com.byarchitect.operator.data.model.ProcessLabel
import com.byarchitect.operator.data.system.SystemFetcher
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProcessViewModel @Inject constructor(
    val systemFetcher: SystemFetcher
) : ViewModel() {

    private val _processLabels = MutableStateFlow<List<ProcessLabel>>(listOf(ProcessLabel.PID, ProcessLabel.NAME))
    val processLabels: StateFlow<List<ProcessLabel>> = _processLabels.asStateFlow()

    //private val _uiState = MutableStateFlow(ProcessListState())
    //val uiState: StateFlow<ProcessListState> = _uiState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ProcessListState> = _processLabels
        .flatMapLatest<List<ProcessLabel>, ProcessListState> { labels ->
            systemFetcher.getProcessList(labels)
                .map { resource ->
                    when (resource) {
                        is Resource.Loading -> ProcessListState(isLoading = true)
                        is Resource.Success -> ProcessListState(
                            processes = resource.data ?: emptyList()
                        )

                        is Resource.Error -> ProcessListState(
                            error = resource.messageId ?: R.string.error_unknown
                        )
                    }
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProcessListState()
        )


    fun load(defaultLabels: List<ProcessLabel> = listOf(ProcessLabel.CPU_PERCENTAGE, ProcessLabel.PID, ProcessLabel.NAME, ProcessLabel.BIT)) {
        _processLabels.value = defaultLabels
    }

    fun updateLabels(newLabels: List<ProcessLabel>) {
        _processLabels.value = newLabels
    }

    /*
            if (labels.isEmpty()) {
                flowOf(ProcessListState()) // Empty state
            } else {
                systemFetcher.getProcessList(labels)
                    .map { resource ->
                        when (resource) {
                            is Resource.Loading -> ProcessListState(isLoading = true)
                            is Resource.Success -> ProcessListState(
                                processes = resource.data ?: emptyList()
                            )
                            is Resource.Error -> ProcessListState(
                                error = resource.messageId ?: R.string.error_unknown
                            )
                        }
                    }
            }
    */

    /*
    fun loadProcessList(labels: List<ProcessLabel>) {
        systemFetcher.getProcessList(labels).onEach { resource ->
            when (resource) {
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }

                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false, processes = resource.data ?: emptyList(), error = null
                        )
                    }
                }

                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false, error = resource.messageId ?: com.byarchitect.operator.R.string.error_unknown
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
    */


}