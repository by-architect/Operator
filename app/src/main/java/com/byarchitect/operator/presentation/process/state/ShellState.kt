package com.byarchitect.operator.presentation.process.state

import com.byarchitect.operator.common.model.Error

data class ShellState(
    val isLoading: Boolean = true,
    val error: Error? = null,
    val success: Boolean = false
) {

}