package com.byarchitect.operator.common.constant

import androidx.compose.foundation.ScrollState
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.unit.Velocity
import com.byarchitect.operator.presentation.process.viewmodel.ProcessViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ProcessScreenSearchScrollManager(
     val scrollState: ScrollState,
     val coroutineScope: CoroutineScope,
     val viewModel: ProcessViewModel,
     val focusManager: FocusManager
    ) {

    fun closeSearchBar(){
        viewModel.clearSearch()
        focusManager.clearFocus()
        coroutineScope.launch {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }
    fun hideSearchBar(){
        focusManager.clearFocus()
        coroutineScope.launch {
            scrollState.scrollTo(scrollState.maxValue)
        }
    }


    val scrollSettings = object : NestedScrollConnection {
        // Disabled to prevent scroll blocking - LazyColumn handles its own scrolling
    }

}