package com.nouman.notevibe.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nouman.notevibe.domain.model.Priority
import com.nouman.notevibe.presentation.theme.IosBlue
import com.nouman.notevibe.presentation.theme.IosDarkSurface
import com.nouman.notevibe.presentation.theme.IosLightSurface
import com.nouman.notevibe.presentation.theme.IosTextPrimaryDark
import com.nouman.notevibe.presentation.theme.IosTextPrimaryLight
import com.nouman.notevibe.presentation.theme.IosTextSecondaryDark
import com.nouman.notevibe.presentation.theme.IosTextSecondaryLight
import com.nouman.notevibe.presentation.theme.SquirclePill

@Composable
fun PriorityFilterSegmentedControl(
    selectedPriority: Priority?,
    onPrioritySelected: (Priority?) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val containerBg = if (isDark) Color(0xFF1C1C1E) else Color(0xFFE5E5EA)

    val options = listOf(
        null to "All",
        Priority.HIGH to "High",
        Priority.MEDIUM to "Medium",
        Priority.LOW to "Low"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(SquirclePill)
            .background(containerBg)
            .padding(3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { (priority, label) ->
            val isSelected = selectedPriority == priority

            val pillBg by animateColorAsState(
                targetValue = if (isSelected) {
                    if (isDark) Color(0xFF2C2C2E) else IosLightSurface
                } else {
                    Color.Transparent
                },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "pillBgAnim"
            )

            val textColor by animateColorAsState(
                targetValue = if (isSelected) {
                    if (isDark) IosTextPrimaryDark else IosTextPrimaryLight
                } else {
                    if (isDark) IosTextSecondaryDark else IosTextSecondaryLight
                },
                label = "textColorAnim"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(SquirclePill)
                    .background(pillBg)
                    .springClickable(onClick = { onPrioritySelected(priority) })
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = textColor,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
