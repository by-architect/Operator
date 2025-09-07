package com.byarchitect.operator.presentation.process.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.byarchitect.operator.R
import com.byarchitect.operator.common.model.Error
import com.byarchitect.operator.common.model.errorResource
import com.byarchitect.operator.data.system.SystemFetcher
import com.byarchitect.operator.presentation.process.viewmodel.ProcessViewModel
import com.byarchitect.operator.presentation.process.widget.ScrollableDataTable
import com.byarchitect.operator.presentation.process.widget.SearchBarRow


@Composable
fun ProcessScreen(
) {

    val repository = SystemFetcher()
    val viewModel: ProcessViewModel = viewModel {
        ProcessViewModel(repository)
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val focusManager = LocalFocusManager.current

            val uiState by viewModel.processState.collectAsState()
            val shellState by viewModel.shellState.collectAsState()
            val processLabelList by viewModel.processLabels.collectAsState()
            val searchQuery by viewModel.searchQuery.collectAsState()


            when {
                shellState.isLoading -> CircularProgressIndicator()
                shellState.error != null -> Column {
                    Text(errorResource(shellState.error ?: Error.unknownError()))
                    Button(onClick = {
                        viewModel.loadShell()
                    }) { Text(stringResource(R.string.load_again)) }
                }

                else -> when {
                    uiState.isLoading -> {
                        CircularProgressIndicator()
                    }

                    uiState.error != null -> {
                        Text(errorResource(uiState.error!!))
                    }

                    else -> {
                        Box(Modifier.height(12.dp))
                        SearchBarRow( viewModel = viewModel,focusManager = focusManager, searchValue = searchQuery)
                        Box(modifier = Modifier.height(14.dp))
                        ScrollableDataTable(processLabelList = processLabelList, data = uiState.processes,focusManager = focusManager, viewModel = viewModel)
                    }
                }
            }

        }
    }

}