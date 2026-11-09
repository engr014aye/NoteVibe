package com.nouman.notevibe.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = IosBlue,
    onPrimary = IosLightSurface,
    primaryContainer = IosBlueSubtle,
    onPrimaryContainer = IosBlue,
    background = IosLightBg,
    onBackground = IosTextPrimaryLight,
    surface = IosLightSurface,
    onSurface = IosTextPrimaryLight,
    surfaceVariant = IosLightSurfaceTranslucent,
    onSurfaceVariant = IosTextSecondaryLight,
    outline = GlassBorderLight
)

private val DarkColorScheme = darkColorScheme(
    primary = IosBlue,
    onPrimary = IosTextPrimaryDark,
    primaryContainer = IosBlueSubtle,
    onPrimaryContainer = IosBlue,
    background = IosDarkBg,
    onBackground = IosTextPrimaryDark,
    surface = IosDarkSurface,
    onSurface = IosTextPrimaryDark,
    surfaceVariant = IosDarkSurfaceTranslucent,
    onSurfaceVariant = IosTextSecondaryDark,
    outline = GlassBorderDark
)

@Composable
fun NoteVibeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = NoteVibeShapes,
        content = content
    )
}
