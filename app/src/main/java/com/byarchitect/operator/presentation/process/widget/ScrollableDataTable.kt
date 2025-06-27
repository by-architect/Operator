package com.byarchitect.operator.presentation.process.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.byarchitect.operator.R
import com.byarchitect.operator.data.model.ProcessLabel
import com.byarchitect.operator.presentation.process.viewmodel.ProcessViewModel

@Composable
fun ScrollableDataTable(
    processLabelList: List<ProcessLabel>,
    data: List<Map<ProcessLabel, String>>,
    modifier: Modifier = Modifier,
    viewModel: ProcessViewModel
) {
    if (data.isEmpty()) return

    val scrollState = rememberScrollState()

    val sortOrderState by viewModel.sortOrder.collectAsState()
// State to track selected PID
    var selectedPID by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                processLabelList.forEachIndexed { index, header ->
                    if (index == 1)
                        HeaderBox(
                            header.label, modifier = Modifier
                                .width(160.dp)
                                .clickable(onClick = {
                                    viewModel.sortProcess(header)
                                }),
                            ascending = if (sortOrderState.label == header) sortOrderState.isAscending else null
                        )
                    else
                        HeaderBox(
                            header.label, modifier = Modifier
                                .width(70.dp)
                                .clickable(onClick = {
                                    viewModel.sortProcess(header)
                                }),
                            ascending = if (sortOrderState.label == header) sortOrderState.isAscending else null
                        )
                }
            }
        }

        // Scrollable Content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            itemsIndexed(data) { index, row ->
                val rowPID = row[ProcessLabel.PID] // Assuming ProcessLabel.PID is your PID key
                val isSelected = selectedPID == rowPID

                Card(
                    onClick = {
                        // Select only one row - toggle if same row, otherwise select new row
                        selectedPID = if (isSelected) null else rowPID
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            Color(0xFFE3F2FD) // Light blue when selected
                        else
                            Color(0xFFF5F5F5)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        processLabelList.forEachIndexed { index, header ->
                            if (index == 1)
                                Box(
                                    modifier = Modifier
                                        .width(160.dp)
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
                            else
                                Box(
                                    modifier = Modifier
                                        .width(70.dp)
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

        if (selectedPID != null) {
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .background(color = Color.Black),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ElevatedButton(
                    onClick = {
                        selectedPID?.let { pid ->
                            viewModel.killProcess(pid.toInt())
                        }
                        selectedPID = null
                    }
                ) {
                    Text(text = stringResource(R.string.exit))
                }
            }
        }
    }
}

@Composable
fun HeaderBox(label: String, modifier: Modifier = Modifier, ascending: Boolean? = null) {
    Box(
        modifier = modifier
            .height(20.dp)
            .padding(end = 8.dp),
        contentAlignment = Alignment.Center,


        ) {
        Row {

            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )
            if (ascending != null)
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = if (ascending ) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}
