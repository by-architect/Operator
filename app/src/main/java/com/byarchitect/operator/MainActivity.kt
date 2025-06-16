package com.byarchitect.operator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.getValue

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.byarchitect.operator.common.model.errorResource
import com.byarchitect.operator.common.util.test
import com.byarchitect.operator.data.model.ProcessLabel
import com.byarchitect.operator.data.system.ShellManager
import com.byarchitect.operator.data.system.SystemFetcher
import com.byarchitect.operator.presentation.process.ProcessViewModel
import com.byarchitect.operator.presentation.ui.theme.OperatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            OperatorTheme {
                val repository = SystemFetcher()
                val processViewModel: ProcessViewModel = viewModel {
                    ProcessViewModel(repository)
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        ProcessScreen(processViewModel)

                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Composable
fun ProcessScreen(
    viewModel: ProcessViewModel = hiltViewModel<ProcessViewModel>()
) {
    val uiState by viewModel.uiState.collectAsState()
    val processLabelList by viewModel.processLabels.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load()
    }


/*
    ElevatedButton(modifier = Modifier.fillMaxWidth().height(50.dp),onClick ={
        viewModel.load(listOf(ProcessLabel.PID, ProcessLabel.NAME, ProcessLabel.NAME))
    }) { Text("Hello There")}
*/

    when {
        uiState.isLoading -> {
            CircularProgressIndicator()
        }
        uiState.error != null -> {

            Text(errorResource(uiState.error!!))

        }
        else -> {

            ScrollableDataTable(processLabelList =processLabelList ,data = uiState.processes, viewModel = viewModel)

        }
    }
}

@Composable
fun ScrollableDataTable(
    processLabelList: List<ProcessLabel>,
    data: List<Map<ProcessLabel, String>>,
    modifier: Modifier = Modifier,
    viewModel: ProcessViewModel
) {
    if (data.isEmpty()) return

    val scrollState = rememberScrollState()

    Column(modifier = modifier.fillMaxSize()) {
        // Fixed Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(16.dp)
            ) {
                processLabelList.forEach { header ->
                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .padding(end = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = header.label,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Scrollable Content
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(data) { index, row ->
                Card(
                    onClick = ({
                       viewModel.killProcess(row[ProcessLabel.PID]!!.toInt())
                    }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)

                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        processLabelList.forEach { header ->
                            Box(
                                modifier = Modifier
                                    .width(120.dp)
                                    .padding(end = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = row[header] ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OperatorTheme {
        Greeting("Android")
    }
}