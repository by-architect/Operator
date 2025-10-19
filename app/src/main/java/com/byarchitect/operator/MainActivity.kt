package com.byarchitect.operator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.byarchitect.operator.common.constant.ExternalLinks
import com.byarchitect.operator.presentation.process.screen.ProcessScreen
import com.byarchitect.operator.presentation.settings.screen.SettingsScreen
import com.byarchitect.operator.presentation.ui.theme.OperatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            OperatorTheme {
                var currentScreen by remember { mutableStateOf("process") }

                when (currentScreen) {
                    "process" -> ProcessScreen(
                        onNavigateToSettings = { currentScreen = "settings" }
                    )
                    "settings" -> SettingsScreen(
                        onNavigateBack = { currentScreen = "process" },
                        onNavigateToSourceCode = { openUrl(ExternalLinks.GITHUB_PROJECT) },
                        onNavigateToLicense = { openUrl(ExternalLinks.GITHUB_LICENSE) },
                        onNavigateToAbout = { /* TODO */ }
                    )
                }
            }
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}


