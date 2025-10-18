package com.byarchitect.operator.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.byarchitect.operator.common.constant.Colors
import com.byarchitect.operator.ui.theme.Typography

private val DarkColorScheme = darkColorScheme(
    primary = Colors.Primary,
    onPrimary = Colors.OnPrimary,
    primaryContainer = Colors.PrimaryContainer,
    onPrimaryContainer = Colors.OnPrimaryContainer,
    secondary = Colors.Secondary,
    onSecondary = Colors.OnSecondary,
    secondaryContainer = Colors.SecondaryContainer,
    onSecondaryContainer = Colors.OnSecondaryContainer,
    tertiary = Colors.Tertiary,
    onTertiary = Colors.OnTertiary,
    tertiaryContainer = Colors.TertiaryContainer,
    onTertiaryContainer = Colors.OnTertiaryContainer,
    error = Colors.Error,
    onError = Colors.OnError,
    errorContainer = Colors.ErrorContainer,
    onErrorContainer = Colors.OnErrorContainer,
    background = Colors.Background,
    onBackground = Colors.OnBackground,
    surface = Colors.Surface,
    onSurface = Colors.OnSurface,
    surfaceVariant = Colors.SurfaceVariant,
    onSurfaceVariant = Colors.OnSurfaceVariant,
    outline = Colors.Outline,
    outlineVariant = Colors.OutlineVariant
)

private val LightColorScheme = lightColorScheme(
    primary = Colors.Primary,
    onPrimary = Colors.OnPrimary,
    primaryContainer = Colors.PrimaryContainer,
    onPrimaryContainer = Colors.OnPrimaryContainer,
    secondary = Colors.Secondary,
    onSecondary = Colors.OnSecondary,
    secondaryContainer = Colors.SecondaryContainer,
    onSecondaryContainer = Colors.OnSecondaryContainer,
    tertiary = Colors.Tertiary,
    onTertiary = Colors.OnTertiary,
    tertiaryContainer = Colors.TertiaryContainer,
    onTertiaryContainer = Colors.OnTertiaryContainer,
    error = Colors.Error,
    onError = Colors.OnError,
    errorContainer = Colors.ErrorContainer,
    onErrorContainer = Colors.OnErrorContainer,
    background = Colors.Background,
    onBackground = Colors.OnBackground,
    surface = Colors.Surface,
    onSurface = Colors.OnSurface,
    surfaceVariant = Colors.SurfaceVariant,
    onSurfaceVariant = Colors.OnSurfaceVariant,
    outline = Colors.Outline,
    outlineVariant = Colors.OutlineVariant
)

@Composable
fun OperatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}