package com.byarchitect.operator.common.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource


data class Error(
    val messageResourceId: Int,
    val exception: Exception?,
) {

    companion object {
        fun unknownError(): Error {
            return Error(com.byarchitect.operator.R.string.error_unknown, null)
        }
    }
}



@Composable
fun errorResource(error: Error): String {
    return if (error.exception != null) {
        stringResource(error.messageResourceId) + ": " + error.exception.localizedMessage
    } else
        stringResource(error.messageResourceId)
}