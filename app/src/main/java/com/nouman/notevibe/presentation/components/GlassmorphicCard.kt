package com.nouman.notevibe.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nouman.notevibe.presentation.theme.GlassBorderDark
import com.nouman.notevibe.presentation.theme.GlassBorderLight
import com.nouman.notevibe.presentation.theme.IosDarkSurfaceTranslucent
import com.nouman.notevibe.presentation.theme.IosLightSurfaceTranslucent
import com.nouman.notevibe.presentation.theme.SquircleLarge

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    shape: Shape = SquircleLarge,
    borderWidth: Dp = 1.dp,
    backgroundColor: Color? = null,
    borderColor: Color? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val defaultBg = if (isDark) IosDarkSurfaceTranslucent else IosLightSurfaceTranslucent
    val defaultBorder = if (isDark) GlassBorderDark else GlassBorderLight

    val finalBg = backgroundColor ?: defaultBg
    val finalBorder = borderColor ?: defaultBorder

    Box(
        modifier = modifier
            .clip(shape)
            .background(finalBg, shape)
            .border(borderWidth, finalBorder, shape),
        content = content
    )
}
