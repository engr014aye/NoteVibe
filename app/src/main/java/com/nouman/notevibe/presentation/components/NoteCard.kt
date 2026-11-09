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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Restore
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
import com.nouman.notevibe.presentation.theme.IosTextPrimaryDark
import com.nouman.notevibe.presentation.theme.IosTextPrimaryLight
import com.nouman.notevibe.presentation.theme.IosTextSecondaryDark
import com.nouman.notevibe.presentation.theme.IosTextSecondaryLight
import com.nouman.notevibe.presentation.theme.IosTextTertiaryLight
import com.nouman.notevibe.presentation.theme.SquircleLarge
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onToggleCompletion: () -> Unit,
    onTogglePin: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    onRestore: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val textPrimary = if (isDark) IosTextPrimaryDark else IosTextPrimaryLight
    val textSecondary = if (isDark) IosTextSecondaryDark else IosTextSecondaryLight
    val accent = note.priority.accentColor()

    val formattedDate = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(note.updatedAt))

    GlassmorphicCard(
        modifier = modifier
            .fillMaxWidth()
            .springClickable(onClick = onClick),
        shape = SquircleLarge,
        borderWidth = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Priority, Date, Pin/Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PriorityBadge(priority = note.priority, showLabel = true)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.labelSmall,
                        color = IosTextTertiaryLight
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Pin to Focus Board Toggle (only for active notes)
                    if (onTogglePin != null && !note.isArchived) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    if (note.isPinnedToFocus) accent.copy(alpha = 0.15f) else Color.Transparent
                                )
                                .springClickable(onClick = onTogglePin),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (note.isPinnedToFocus) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                                contentDescription = if (note.isPinnedToFocus) "Unpin from Focus" else "Pin to Focus",
                                tint = if (note.isPinnedToFocus) accent else textSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Restore button for archived notes
                    if (onRestore != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(IosBlue.copy(alpha = 0.12f))
                                .springClickable(onClick = onRestore),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Restore,
                                contentDescription = "Restore Note",
                                tint = IosBlue,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Delete button
                    if (onDelete != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .springClickable(onClick = onDelete),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete Note",
                                tint = textSecondary.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Body + Checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                if (!note.isArchived) {
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(if (note.isCompleted) accent else Color.Transparent)
                            .border(
                                width = 1.8.dp,
                                color = if (note.isCompleted) accent else textSecondary.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                            .springClickable(onClick = onToggleCompletion),
                        contentAlignment = Alignment.Center
                    ) {
                        CompletedCheckIcon(
                            isCompleted = note.isCompleted,
                            size = 14.dp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (note.title.isNotBlank()) note.title else "Untitled Note",
                        style = MaterialTheme.typography.titleSmall,
                        color = if (note.isCompleted) textSecondary else textPrimary,
                        textDecoration = if (note.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = 1,
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

                    // Show auto archive badge if completed
                    if (note.isCompleted && note.daysUntilArchive != null && !note.isArchived) {
                        Spacer(modifier = Modifier.height(6.dp))
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
