package com.byarchitect.operator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.byarchitect.operator.presentation.process.screen.ProcessScreen
import com.byarchitect.operator.presentation.ui.theme.OperatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            OperatorTheme {
                ProcessScreen()
            }
        }
    }
}


