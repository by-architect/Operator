package com.byarchitect.operator.presentation.process.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.byarchitect.operator.R
import com.byarchitect.operator.common.constant.ProcessScreenSearchScrollManager
import com.byarchitect.operator.data.model.ProcessLabel
import com.byarchitect.operator.presentation.process.viewmodel.ProcessViewModel

@Composable
fun ScrollableDataTable(
    processLabelList: List<ProcessLabel>,
    data: List<Map<ProcessLabel, String>>,
    modifier: Modifier = Modifier,
    mainScreenSearchScrollManager: ProcessScreenSearchScrollManager,
    viewModel: ProcessViewModel
) {

    val scrollState = rememberScrollState()

    val sortOrderState by viewModel.sortOrder.collectAsState()

    val processLabelList = listOf(ProcessLabel.NAME,  ProcessLabel.CPU_PERCENTAGE, ProcessLabel.MEM_PERCENTAGE,ProcessLabel.PID)

    var selectedPID by remember { mutableStateOf<String?>(null) }
    var selectedLabel by remember { mutableStateOf<String?>(null) }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(modifier = modifier.fillMaxWidth().height(screenHeight)) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Black),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 20.dp , vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {



                processLabelList.forEachIndexed { index, header ->
                    if (index == 0)
                        HeaderBox(
                            header.label, modifier = Modifier
                                .weight(2f)
                                .clickable(onClick = {
                                    viewModel.sortProcess(header)
                                }),
                            ascending = if (sortOrderState.label == header) sortOrderState.isAscending else null
                        )
                    else
                        HeaderBox(
                            header.label, modifier = Modifier
                                .weight(1f)
                                .clickable(onClick = {
                                    viewModel.sortProcess(header)
                                }),
                            ascending = if (sortOrderState.label == header) sortOrderState.isAscending else null
                        )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            itemsIndexed(data) { index, row ->
                val rowPID = row[ProcessLabel.PID]
                val isSelected = selectedPID == rowPID

                ProcessItemCard(
                    processData = row,
                    isSelected = isSelected,
                    onClick = {
                        mainScreenSearchScrollManager.hideSearchBar()
                        if (isSelected) {
                            selectedPID = null
                            selectedLabel = null
                        } else {
                            selectedPID = rowPID
                            selectedLabel = row[ProcessLabel.NAME]
                        }
                    }
                )
            }
        }

        if (selectedPID != null) {
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .background(color = Color.Black)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(selectedLabel ?: "", color = Color.White,modifier = Modifier.weight(1f).padding(start = 20.dp))
                ElevatedButton(
                    onClick = {
                        selectedPID?.let { pid ->
                            viewModel.killProcess(pid.toInt())
                        }
                        selectedPID = null
                        selectedLabel = null
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
            .height(20.dp),
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
