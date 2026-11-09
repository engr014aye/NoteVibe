package com.nouman.notevibe.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.presentation.theme.IosBlue
import com.nouman.notevibe.presentation.theme.IosDarkSurface
import com.nouman.notevibe.presentation.theme.IosLightSurface
import com.nouman.notevibe.presentation.theme.IosTextPrimaryDark
import com.nouman.notevibe.presentation.theme.IosTextPrimaryLight
import com.nouman.notevibe.presentation.theme.IosTextSecondaryDark
import com.nouman.notevibe.presentation.theme.IosTextSecondaryLight
import com.nouman.notevibe.presentation.theme.IosTextTertiaryLight
import com.nouman.notevibe.presentation.theme.SquircleLarge
import com.nouman.notevibe.presentation.theme.SquircleMedium
import com.nouman.notevibe.presentation.theme.SquirclePill

@Composable
fun FocusSlotCard(
    slotNumber: Int,
    note: Note?,
    onCardClick: () -> Unit,
    onToggleCompletion: () -> Unit,
    onUnpin: () -> Unit,
    onEmptySlotClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val textPrimary = if (isDark) IosTextPrimaryDark else IosTextPrimaryLight
    val textSecondary = if (isDark) IosTextSecondaryDark else IosTextSecondaryLight

    if (note == null) {
        // Empty Focus Slot
        GlassmorphicCard(
            modifier = modifier
                .fillMaxWidth()
                .springClickable(onClick = onEmptySlotClick),
            shape = SquircleLarge,
            borderWidth = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isDark) Color(0xFF2C2C2E) else Color(0xFFE5E5EA)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Assign Focus Slot",
                        tint = textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Focus Slot #$slotNumber",
                        style = MaterialTheme.typography.titleSmall,
                        color = textSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Tap to assign priority task",
                        style = MaterialTheme.typography.bodySmall,
                        color = IosTextTertiaryLight,
                        fontSize = 12.sp
                    )
                }
            }
        }
    } else {
        // Occupied Focus Slot Card
        val accent = note.priority.accentColor()

        GlassmorphicCard(
            modifier = modifier
                .fillMaxWidth()
                .springClickable(onClick = onCardClick),
            shape = SquircleLarge,
            borderColor = accent.copy(alpha = 0.35f),
            borderWidth = 1.5.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header: Slot Indicator & Priority & Pin/Unpin
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(SquirclePill)
                                .background(accent.copy(alpha = 0.15f))
                                .padding(horizontal = 9.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "FOCUS #$slotNumber",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = accent,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        PriorityBadge(priority = note.priority, showLabel = true)
                    }

                    // Quick Unpin Action
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(accent.copy(alpha = 0.12f))
                            .springClickable(onClick = onUnpin),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PushPin,
                            contentDescription = "Unpin from Focus Board",
                            tint = accent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title + Completion Checkbox
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    // Tactile Circular Checkbox
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                if (note.isCompleted) accent else Color.Transparent
                            )
                            .border(
                                width = 2.dp,
                                color = if (note.isCompleted) accent else textSecondary.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                            .springClickable(onClick = onToggleCompletion),
                        contentAlignment = Alignment.Center
                    ) {
                        CompletedCheckIcon(
                            isCompleted = note.isCompleted,
                            size = 15.dp
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (note.title.isNotBlank()) note.title else "Untitled Note",
                            style = MaterialTheme.typography.titleMedium,
                            color = if (note.isCompleted) textSecondary else textPrimary,
                            textDecoration = if (note.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (note.content.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = note.content,
                                style = MaterialTheme.typography.bodySmall,
                                color = textSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                textDecoration = if (note.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                            )
                        }

                        // Days until auto-archive hint if completed
                        if (note.isCompleted && note.daysUntilArchive != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            val archiveHint = when (note.daysUntilArchive) {
                                0 -> "Auto-archives today"
                                1 -> "Auto-archives tomorrow"
                                else -> "Auto-archives in ${note.daysUntilArchive} days"
                            }
                            Text(
                                text = archiveHint,
                                fontSize = 11.sp,
                                color = textSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
