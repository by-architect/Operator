package com.byarchitect.operator.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import com.byarchitect.operator.ui.theme.Typography

/**
 * Dark color scheme for the Operator app.
 * All colors are sourced from the Colors object.
 */
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
    outlineVariant = Colors.OutlineVariant,
    inverseSurface = Colors.White,
    inverseOnSurface = Colors.Black,
    inversePrimary = Colors.Primary,
    surfaceTint = Colors.Primary,
    scrim = Colors.Black
)

/**
 * Operator theme - Dark theme only.
 * Always uses the dark color scheme regardless of system settings.
 */
@Composable
fun OperatorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}