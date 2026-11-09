package com.nouman.notevibe.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nouman.notevibe.domain.model.Priority
import com.nouman.notevibe.presentation.theme.PriorityHighBg
import com.nouman.notevibe.presentation.theme.PriorityHighColor
import com.nouman.notevibe.presentation.theme.PriorityLowBg
import com.nouman.notevibe.presentation.theme.PriorityLowColor
import com.nouman.notevibe.presentation.theme.PriorityMediumBg
import com.nouman.notevibe.presentation.theme.PriorityMediumColor
import com.nouman.notevibe.presentation.theme.SquirclePill

@Composable
fun PriorityBadge(
    priority: Priority,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true
) {
    val (dotColor, bgColor, textColor) = when (priority) {
        Priority.HIGH -> Triple(PriorityHighColor, PriorityHighBg, PriorityHighColor)
        Priority.MEDIUM -> Triple(PriorityMediumColor, PriorityMediumBg, PriorityMediumColor)
        Priority.LOW -> Triple(PriorityLowColor, PriorityLowBg, PriorityLowColor)
    }

    Row(
        modifier = modifier
            .clip(SquirclePill)
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
        if (showLabel) {
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = priority.displayName.replace(" Priority", ""),
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

fun Priority.accentColor(): Color = when (this) {
    Priority.HIGH -> PriorityHighColor
    Priority.MEDIUM -> PriorityMediumColor
    Priority.LOW -> PriorityLowColor
}
