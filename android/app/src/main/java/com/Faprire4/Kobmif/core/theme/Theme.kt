package com.Faprire4.Kobmif.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CourtColorScheme = darkColorScheme(
    primary = AntiqueGold,
    onPrimary = VelvetDeep,
    primaryContainer = BurgundyAccent,
    onPrimaryContainer = Candlelight,
    secondary = Candlelight,
    onSecondary = VelvetDeep,
    secondaryContainer = SurfaceRaised,
    onSecondaryContainer = TextPrimary,
    tertiary = BurgundyAccent,
    onTertiary = TextPrimary,
    background = BackgroundDeep,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    outline = NeutralSeal,
    error = Crimson,
    onError = TextPrimary
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CourtColorScheme,
        typography = AppTypography,
        content = content
    )
}
