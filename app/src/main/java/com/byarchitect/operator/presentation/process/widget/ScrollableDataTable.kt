package com.byarchitect.operator.presentation.process.widget

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.byarchitect.operator.data.model.ProcessLabel
import com.byarchitect.operator.presentation.process.state.ProcessViewModel

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
                        //viewModel.killProcess(row[ProcessLabel.PID]!!.toInt())
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
                                    color = Color.Black,
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
