package com.byarchitect.operator.presentation.process.state

import com.byarchitect.operator.common.model.Error
import com.byarchitect.operator.data.model.ProcessLabel

data class ProcessListState(
    val isLoading: Boolean = false,
    val processes: List<Map<ProcessLabel, String>> = emptyList(),
    val error: Error? = null
)