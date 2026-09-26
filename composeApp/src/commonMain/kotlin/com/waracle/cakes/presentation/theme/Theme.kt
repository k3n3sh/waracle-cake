package com.waracle.cakes.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Charcoal,
    onPrimary = Almond,
    primaryContainer = Charcoal,
    onPrimaryContainer = Almond,
    secondary = CharcoalLight,
    onSecondary = Almond,
    background = Almond,
    onBackground = Charcoal,
    surface = Almond,
    onSurface = Charcoal,
    surfaceVariant = AlmondDark,
    onSurfaceVariant = CharcoalLight,
    surfaceContainerHigh = Almond,
    outlineVariant = AlmondDark,
    error = ErrorRed,
)

private val DarkColors = darkColorScheme(
    primary = Almond,
    onPrimary = Charcoal,
    primaryContainer = CharcoalLight,
    onPrimaryContainer = Almond,
    secondary = AlmondDark,
    onSecondary = Charcoal,
    background = Charcoal,
    onBackground = Almond,
    surface = Charcoal,
    onSurface = Almond,
    surfaceVariant = CharcoalLight,
    onSurfaceVariant = AlmondDark,
    surfaceContainerHigh = CharcoalLight,
    outlineVariant = CharcoalLight,
    error = ErrorRedLight,
)

@Composable
fun CakesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    // TODO: brand typography
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
