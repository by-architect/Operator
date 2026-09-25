package com.byarchitect.operator.presentation.settings.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.PackageInfoCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.byarchitect.operator.R
import com.byarchitect.operator.common.constant.ExternalLinks
import com.byarchitect.operator.data.repository.SettingsHandler
import com.byarchitect.operator.data.system.Backend
import com.byarchitect.operator.data.system.SystemAccess
import com.byarchitect.operator.presentation.settings.viewmodel.SettingsViewModel
import com.byarchitect.operator.presentation.settings.widget.SettingsNumberOptionRow
import com.byarchitect.operator.presentation.settings.widget.SettingsRow

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    onNavigateToSourceCode: () -> Unit = {},
    onNavigateToLicense: () -> Unit = {},
    onNavigateToIssues: () -> Unit = {}
) {
    val settingsHandler = SettingsHandler(LocalContext.current.applicationContext)
    val viewModel: SettingsViewModel = viewModel {
        SettingsViewModel(settingsHandler)
    }

    val refreshRate by viewModel.refreshInterval.collectAsState()
    val context = LocalContext.current
    var showAbout by remember { mutableStateOf(false) }

    // Handle system back button
    BackHandler(onBack = onNavigateBack)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            // The window is edge-to-edge, so inset by the real status bar height.
            // A fixed 24dp left the title sitting under the clock on taller cutouts.
            .statusBarsPadding(),
    ) {
        // Top Bar with Back Button and Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button on left
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Title in center
                Text(
                    text = stringResource(R.string.settings),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(vertical = 16.dp)
        ) {
            // General Section
            SectionLabel(text = stringResource(R.string.general))

        SettingsNumberOptionRow(
            label = stringResource(R.string.refresh_rate),
            icon = Icons.Default.Refresh,
            value = (refreshRate).toString(),
            onValueChange = {
                viewModel.setRefreshInterval(it)
            },
            onApply = { viewModel.saveIntervalToDatabase(context) },
            applyButtonText = stringResource(R.string.apply)
        )


        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        // About Section
        SectionLabel(text = stringResource(R.string.about))

        SettingsRow(
            label = stringResource(R.string.source_code),
            icon = Icons.Default.Code,
            onClick = onNavigateToSourceCode
        )

        SettingsRow(
            label = stringResource(R.string.license),
            icon = Icons.Default.LocalPolice,
            onClick = onNavigateToLicense
        )

            SettingsRow(
                label = stringResource(R.string.report_issue),
                icon = Icons.Default.BugReport,
                onClick = onNavigateToIssues
            )

            SettingsRow(
                label = stringResource(R.string.about_app),
                icon = Icons.Default.Info,
                onClick = { showAbout = true }
            )
        }
    }

    if (showAbout) {
        AboutDialog(onDismiss = { showAbout = false })
    }
}

@Composable
private fun AboutDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val info = remember {
        runCatching {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, 0)
        }.getOrNull()
    }
    val version = info?.versionName ?: "-"
    val build = info?.let { PackageInfoCompat.getLongVersionCode(it).toString() } ?: "-"

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                // primary is #1A1A1A, the same as the background, so the default
                // TextButton content colour is unreadable on any surface.
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) { Text(stringResource(R.string.close)) }
        },
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column {
                AboutLine(stringResource(R.string.version), "$version ($build)")
                AboutLine(stringResource(R.string.package_name), context.packageName)
                AboutLine(
                    stringResource(R.string.backend),
                    stringResource(
                        when (SystemAccess.backend) {
                            Backend.ROOT -> R.string.backend_root
                            Backend.SHIZUKU -> R.string.backend_shizuku
                            Backend.NONE -> R.string.backend_none
                        }
                    )
                )
                AboutLine(stringResource(R.string.license), "GPL-3.0-only")
                AboutLine(stringResource(R.string.author), "by-architect")
                AboutLine(stringResource(R.string.contact), ExternalLinks.CONTACT_EMAIL)
                Text(
                    text = stringResource(R.string.requires_root_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun AboutLine(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.fillMaxWidth(0.38f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold
        ),
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
