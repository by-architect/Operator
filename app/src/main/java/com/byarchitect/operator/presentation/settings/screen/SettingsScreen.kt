package com.byarchitect.operator.presentation.settings.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.byarchitect.operator.R
import com.byarchitect.operator.data.repository.SettingsHandler
import com.byarchitect.operator.presentation.settings.viewmodel.SettingsViewModel
import com.byarchitect.operator.presentation.settings.widget.SettingsNumberOptionRow
import com.byarchitect.operator.presentation.settings.widget.SettingsRow

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavigateToSourceCode: () -> Unit = {},
    onNavigateToLicense: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {}
) {
    val settingsHandler = SettingsHandler(LocalContext.current.applicationContext)
    val viewModel: SettingsViewModel = viewModel {
        SettingsViewModel(settingsHandler)
    }

    val refreshRate by viewModel.refreshInterval.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
            onApply = { viewModel.saveIntervalToDatabase() },
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
            icon = Icons.Default.Build,
            onClick = onNavigateToSourceCode
        )

        SettingsRow(
            label = stringResource(R.string.license),
            icon = Icons.Default.Info,
            onClick = onNavigateToLicense
        )

        SettingsRow(
            label = stringResource(R.string.about_app),
            icon = Icons.Default.Info,
            onClick = onNavigateToAbout
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
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
