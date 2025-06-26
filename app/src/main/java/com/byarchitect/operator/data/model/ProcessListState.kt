package com.byarchitect.operator.data.model

import com.byarchitect.operator.common.model.Error

data class ProcessListState(
    val isLoading: Boolean = false,
    val processes: List<Map<ProcessLabel, String>> = emptyList(),
    val sortBy: ProcessLabel = ProcessLabel.PID,
    val ascending: Boolean = false,
    val error: Error? = null
)