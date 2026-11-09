package com.nouman.notevibe.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Squircle-like continuous corner shapes conforming to iOS Human Interface Guidelines
val SquircleSmall = RoundedCornerShape(14.dp)
val SquircleMedium = RoundedCornerShape(20.dp)
val SquircleLarge = RoundedCornerShape(26.dp)
val SquircleExtraLarge = RoundedCornerShape(32.dp)
val SquirclePill = RoundedCornerShape(50)

val NoteVibeShapes = Shapes(
    small = SquircleSmall,
    medium = SquircleMedium,
    large = SquircleLarge,
    extraLarge = SquircleExtraLarge
)
