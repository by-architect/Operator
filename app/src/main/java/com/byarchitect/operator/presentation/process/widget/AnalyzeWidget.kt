package com.byarchitect.operator.presentation.process.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.byarchitect.operator.common.util.system.SystemAnalyzer
import com.byarchitect.operator.data.model.TotalProcessUsage

@Composable
fun AnalyzeWidget(totalProcessUsageList: List<TotalProcessUsage>) {
    val cpuThreshold = SystemAnalyzer.findCpuThreshold(totalProcessUsageList.map { it.cpuPercentage.toDouble() })
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(120.dp)) {
        MainCard(if (totalProcessUsageList.isNotEmpty()) totalProcessUsageList.last().cpuPercentage.toString() else "0")
    }
}

@Composable
private fun MainCard(text: String) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)

    ) {
        Text(text)
    }
}
