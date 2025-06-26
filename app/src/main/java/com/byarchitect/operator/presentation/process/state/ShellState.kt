package com.byarchitect.operator.presentation.process.state

import com.byarchitect.operator.common.model.Error

data class ShellState(
    val isRoot: Boolean = false,
    val isLoading: Boolean = false,
    val error: Error? = null
) {

}