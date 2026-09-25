package com.byarchitect.operator.presentation.process.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.byarchitect.operator.R
import com.byarchitect.operator.common.constant.ProcessScreenSearchScrollManager
import com.byarchitect.operator.common.model.Error
import com.byarchitect.operator.common.model.errorResource
import com.byarchitect.operator.data.repository.SettingsHandler
import com.byarchitect.operator.data.system.ProcessManager
import com.byarchitect.operator.data.system.SystemAccess
import com.byarchitect.operator.data.system.SystemFetcher
import com.byarchitect.operator.presentation.process.viewmodel.ProcessViewModel
import com.byarchitect.operator.presentation.process.widget.ScrollableDataTable
import com.byarchitect.operator.presentation.process.widget.SearchBarRow
import com.byarchitect.operator.presentation.process.widget.SelectedProcessContainer


@Composable
fun ProcessScreen(
    onNavigateToSettings: () -> Unit = {}
) {

    val context = androidx.compose.ui.platform.LocalContext.current
    val repository = SystemFetcher()
    val processManager = ProcessManager()
    val settingsHandler = SettingsHandler(context.applicationContext)
    val viewModel: ProcessViewModel = viewModel {
        ProcessViewModel(repository, processManager, settingsHandler)
    }

    val scrollManager = ProcessScreenSearchScrollManager(
        viewModel = viewModel,
        coroutineScope = rememberCoroutineScope(),
        scrollState = rememberScrollState(Int.MAX_VALUE),
        focusManager = LocalFocusManager.current
    )

    DisposableEffect(Unit) {
        val handle = SystemAccess.addPermissionResultListener { granted ->
            if (granted) viewModel.loadShell()
        }
        onDispose { handle.remove() }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
        ) {


            val uiState by viewModel.processState.collectAsState()
            val shellState by viewModel.shellState.collectAsState()
            val searchQuery by viewModel.searchQuery.collectAsState()
            val selectedProcess by viewModel.selectedProcess.collectAsState()


            when {
                shellState.isLoading -> CenteredState(innerPadding) {
                    CircularProgressIndicator()
                }

                shellState.error != null -> CenteredState(innerPadding) {
                    Text(
                        text = errorResource(shellState.error ?: Error.unknownError()),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))

                    // Offer Shizuku only when it is actually running - suggesting it
                    // otherwise just sends people to a dead end.
                    if (SystemAccess.isShizukuRunning()) {
                        Button(onClick = { SystemAccess.requestShizukuPermission() }) {
                            Text(stringResource(R.string.grant_shizuku))
                        }
                        Spacer(Modifier.height(8.dp))
                    }

                    Button(onClick = {
                        viewModel.loadShell()
                    }) { Text(stringResource(R.string.load_again)) }

                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = stringResource(R.string.no_backend_hint),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center
                    )
                }

                else -> when {
                    uiState.isLoading -> CenteredState(innerPadding) {
                        CircularProgressIndicator()
                    }

                    uiState.error != null -> CenteredState(innerPadding) {
                        Text(
                            text = errorResource(uiState.error!!),
                            textAlign = TextAlign.Center
                        )
                    }

                    else -> {
                        BoxWithConstraints(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            // Size the table to the visible viewport rather than the whole
                            // screen. The scrollable range is then exactly the spacer plus the
                            // search bar, so scrolling to maxValue hides the search bar and
                            // stops with the table header at the top. Using the full screen
                            // height overshoots by the system bar insets and hides the header
                            // too, which is what the list did on first open.
                            val viewportHeight = maxHeight

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .nestedScroll(scrollManager.scrollSettings)
                                    .verticalScroll(scrollManager.scrollState),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(Modifier.height(12.dp))
                                SearchBarRow(
                                    viewModel = viewModel,
                                    mainScreenScrollManager = scrollManager,
                                    searchValue = searchQuery,
                                    onNavigateToSettings = onNavigateToSettings
                                )
                                ScrollableDataTable(
                                    data = uiState.processes,
                                    mainScreenSearchScrollManager = scrollManager,
                                    selectedProcess = selectedProcess,
                                    viewModel = viewModel,
                                    tableHeight = viewportHeight
                                )
                            }

                            if (selectedProcess != null)
                                SelectedProcessContainer(modifier = Modifier.align (Alignment.BottomCenter),viewModel, selectedProcess!!)
                        }
                    }
                }
            }

        }
    }

}

/**
 * Centres a transient state - the loading spinner, or an error with its retry button -
 * in the space the Scaffold gives us. Without this they sit in the top-left corner.
 */
@Composable
private fun CenteredState(
    innerPadding: PaddingValues,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}
