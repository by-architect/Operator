package com.byarchitect.operator.presentation.process.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.byarchitect.operator.R
import com.byarchitect.operator.data.model.Process
import com.byarchitect.operator.presentation.ui.theme.Colors

@Composable
fun ProcessItemCard(
    process: Process,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    // Get app icon drawable
    val appIcon = try {
        context.packageManager.getApplicationIcon(process.packageName)
    } catch (e: Exception) {
        null
    }

    // Get package label
    val packageLabel = try {
        val appInfo = context.packageManager.getApplicationInfo(process.packageName, 0)
        context.packageManager.getApplicationLabel(appInfo).toString()
    } catch (e: Exception) {
        process.packageName.substringAfterLast(".")
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Colors.CardSelected else Colors.CardUnselected
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // First Row: Icon | Label
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App Icon
                if (appIcon != null) {
                    Image(
                        bitmap = appIcon.toBitmap().asImageBitmap(),
                        contentDescription = stringResource(R.string.app_icon),
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(R.string.default_icon),
                        modifier = Modifier.size(40.dp),
                        tint = Colors.IconTint
                    )
                }

                // Package Label
                Text(
                    text = packageLabel,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Colors.TextPrimary
                )
            }
            Box(modifier = Modifier.padding(vertical = 10.dp))

            // Second Row: Package Name | CPU | Memory | PID
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Package Name Column (can drop to next line within column)
                Box (modifier = Modifier.weight(2f)){
                    Text(
                        text = process.packageName,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = Colors.TextSecondary,
                        modifier = Modifier.padding(end = 30.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }


                // CPU Usage Column
                Text(
                    text = process.cpuPercentage.toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.CpuColor,
                    modifier = Modifier.weight(1f)
                )

                // Memory Usage Column
                Text(
                    text = process.memPercentage.toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.MemoryColor,
                    modifier = Modifier.weight(1f)
                )

                // Divider

                // PID Column
                Text(
                    text = process.pid.toString(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.PidColor,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
