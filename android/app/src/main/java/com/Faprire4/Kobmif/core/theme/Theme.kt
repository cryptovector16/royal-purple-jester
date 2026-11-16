package com.Faprire4.Kobmif.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val JesterColorScheme = darkColorScheme(
    primary = AntiqueGold,
    onPrimary = InkBlack,
    primaryContainer = VelvetHighlight,
    onPrimaryContainer = Candlelight,
    secondary = Candlelight,
    onSecondary = InkBlack,
    secondaryContainer = VelvetBurgundy,
    onSecondaryContainer = TextPrimary,
    tertiary = SealCrimson,
    onTertiary = TextPrimary,
    background = InkBlack,
    onBackground = TextPrimary,
    surface = SurfaceCourt,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    outline = InertBorder,
    outlineVariant = GoldBorder,
    error = SealCrimson,
    onError = TextPrimary
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = JesterColorScheme,
        typography = AppTypography,
        content = content
    )
}
