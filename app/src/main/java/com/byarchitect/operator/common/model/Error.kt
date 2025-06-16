package com.byarchitect.operator.common.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.byarchitect.operator.common.util.logTest


data class Error(
    val messageResourceId: Int,
    val exception: Exception?,
) {

    init {
        logTest(title = "Resource Error", data = this)
    }

    companion object {
        fun unknownError(): Error {
            return Error(com.byarchitect.operator.R.string.error_unknown, null)
        }
    }

    override fun toString(): String {
        return "Error(messageResourceId=$messageResourceId, exception=$exception)"
    }
}



@Composable
fun errorResource(error: Error): String {
    return if (error.exception != null) {
        stringResource(error.messageResourceId) + ": " + error.exception.localizedMessage
    } else
        stringResource(error.messageResourceId)
}