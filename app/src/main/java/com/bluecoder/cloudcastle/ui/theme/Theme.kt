package com.bluecoder.cloudcastle.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
    primary = RoyalBlue,
    secondary = Peach,
    tertiary = Teal200
)

private val LightColorPalette = lightColorScheme(
    primary = Orange,
    secondary = SmokeWhite,
    tertiary = Pink,
    background = StrongWhite,
    surface = StrongWhite,
    onPrimary = Black,
    onSecondary = Black,
    onBackground = StrongWhite,
    onSurface = StrongWhite,
)

@Composable
fun CloudCastleTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}