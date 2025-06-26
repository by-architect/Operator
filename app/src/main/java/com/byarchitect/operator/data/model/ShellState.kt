package com.byarchitect.operator.data.model

import com.byarchitect.operator.common.model.Error

data class ShellState(
    val isLoading: Boolean = true,
    val error: Error? = null,
    val success: Boolean = false
) {

}