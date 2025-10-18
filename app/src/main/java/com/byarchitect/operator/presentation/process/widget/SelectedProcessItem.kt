package com.byarchitect.operator.presentation.process.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.byarchitect.operator.R
import com.byarchitect.operator.data.model.SelectedProcessModel
import com.byarchitect.operator.presentation.process.viewmodel.ProcessViewModel

@Composable
fun SelectedProcessContainer(modifier: Modifier,viewModel: ProcessViewModel, selectedProcess: SelectedProcessModel) {
    Row(
        modifier =modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            selectedProcess.label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        ElevatedButton(
            onClick = {
                viewModel.killProcess(selectedProcess.pid.toInt())
            }
        ) {
            Text(text = stringResource(R.string.exit))
        }
    }
}
