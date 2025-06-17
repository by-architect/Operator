package com.byarchitect.operator.presentation.process
data class ShellState(
    val isLoading: Boolean = true,
    val isRoot: Boolean = false,
    val success: Boolean = false,
    val error: Error? = null
) {

}
