package com.byarchitect.operator.common.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import com.byarchitect.operator.common.util.logTest


data class Error(
    @StringRes val messageResource: Int,
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
        return "Error(messageResourceId=$messageResource, exception=$exception)"
    }
}


@Composable
@ReadOnlyComposable
fun errorResource(error: Error): String {
    val resources = LocalContext.current.resources
    return if (error.exception != null)
        resources.getString(error.messageResource) + ": " + error.exception.localizedMessage
    else
        resources.getString(error.messageResource)
}
