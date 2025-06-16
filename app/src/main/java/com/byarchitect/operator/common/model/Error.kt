package com.byarchitect.operator.common.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


data class Error(
    val messageResourceId: Int,
    val exception: String?,
) {

}

@Composable
fun errorResource(error: Error): String {
    return if (error.exception != null) {
        stringResource(error.messageResourceId) + ": " + error.exception
    } else
        stringResource(error.messageResourceId)
}