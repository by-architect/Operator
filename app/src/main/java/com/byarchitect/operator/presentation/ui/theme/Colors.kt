package com.byarchitect.operator.presentation.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Color definitions for the Operator app.
 * Dark theme with grey shades.
 * These colors are also defined in res/values/colors.xml for XML usage and documentation.
 */
object Colors {
    // Primary colors - Dark grey shades
    val Primary = Color(0xFF1A1A1A) // Very dark grey
    val OnPrimary = Color(0xFFFFFFFF)
    val PrimaryContainer = Color(0xFF404040) // Dark grey
    val OnPrimaryContainer = Color(0xFFE0E0E0) // Light grey text

    // Secondary colors - Dark grey shades
    val Secondary = Color(0xFF5A5A5A) // Dark grey
    val OnSecondary = Color(0xFFFFFFFF)
    val SecondaryContainer = Color(0xFF3A3A3A) // Very dark grey
    val OnSecondaryContainer = Color(0xFFB8B8B8) // Light grey text

    // Tertiary colors - Dark grey shades
    val Tertiary = Color(0xFF707070) // Medium-dark grey
    val OnTertiary = Color(0xFFFFFFFF)
    val TertiaryContainer = Color(0xFF2B2B2B) // Very dark grey
    val OnTertiaryContainer = Color(0xFFA1A1A1) // Medium-light grey text

    // Surface colors - Dark grey tinted
    val Surface = Color(0xFF2B2B2B) // Very dark grey for card backgrounds
    val OnSurface = Color(0xFFE0E0E0) // Light grey text
    val SurfaceVariant = Color(0xFF404040) // Dark grey for header
    val OnSurfaceVariant = Color(0xFFE0E0E0) // Light grey for header text

    // Background colors - Dark grey tinted
    val Background = Color(0xFF1A1A1A) // Very dark grey background
    val OnBackground = Color(0xFFE0E0E0) // Light grey text

    // Error colors
    val Error = Color(0xFFCF6679) // Lighter red for dark theme
    val OnError = Color(0xFF000000)
    val ErrorContainer = Color(0xFF4A1C1C) // Dark red container
    val OnErrorContainer = Color(0xFFFFC9C9) // Light red text

    // Outline - Dark grey shades
    val Outline = Color(0xFF5A5A5A) // Dark grey
    val OutlineVariant = Color(0xFF404040) // Dark grey

    // Card colors - Dark grey shades
    val CardSelected = Color(0xFF404040) // Dark grey
    val CardUnselected = Color(0xFF2B2B2B) // Very dark grey

    // Text colors - Light grey shades for dark background
    val TextPrimary = Color(0xFFE0E0E0) // Light grey
    val TextSecondary = Color(0xFFB8B8B8) // Medium-light grey
    val TextLabel = Color(0xFF898989) // Medium grey

    // Icon colors - Light grey shade
    val IconTint = Color(0xFFB8B8B8) // Medium-light grey

    // Process-specific colors
    val CpuColor = Color(0xFFDC9631) // Lighter blue for dark theme
    val MemoryColor = Color(0xFFDC9631) // Lighter orange for dark theme
    val PidColor = Color(0xFF898989) // Medium grey

    // Divider - Dark grey shade
    val DividerColor = Color(0xFF404040) // Dark grey

    // Common colors
    val White = Color(0xFFFFFFFF)
    val Black = Color(0xFF000000)
    val Transparent = Color.Companion.Transparent
}