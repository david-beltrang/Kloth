package com.example.kloth.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = KlothWhite,
    secondary = KlothSecondary,
    background = KlothBlack,
    surface = KlothBlack,
    onPrimary = KlothBlack,
    onSecondary = KlothWhite,
    onBackground = KlothWhite,
    onSurface = KlothWhite,
    onSurfaceVariant = KlothOnSurfaceVariantDark,
    outlineVariant = KlothOutlineVariantDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainer = SurfaceContainerDark,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerLowest = SurfaceContainerLowestDark
)

private val LightColorScheme = lightColorScheme(
    primary = KlothBlack,
    secondary = KlothSecondary,
    background = KlothSurface,
    surface = KlothSurface,
    onPrimary = KlothWhite,
    onSecondary = KlothWhite,
    onBackground = KlothOnSurface,
    onSurface = KlothOnSurface,
    onSurfaceVariant = KlothOnSurfaceVariant,
    secondaryContainer = KlothSecondaryContainer,
    onSecondaryContainer = KlothOnSecondaryContainer,
    outlineVariant = KlothOutlineVariant,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerLowest = SurfaceContainerLowest
)

@Composable
fun KlothTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
