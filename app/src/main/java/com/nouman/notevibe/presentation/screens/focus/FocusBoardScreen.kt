package com.nouman.notevibe.presentation.screens.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
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
import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.presentation.components.FocusSlotCard
import com.nouman.notevibe.presentation.components.GlassmorphicCard
import com.nouman.notevibe.presentation.components.IosTopBar
import com.nouman.notevibe.presentation.theme.IosBlue
import com.nouman.notevibe.presentation.theme.IosTextPrimaryDark
import com.nouman.notevibe.presentation.theme.IosTextPrimaryLight
import com.nouman.notevibe.presentation.theme.IosTextSecondaryDark
import com.nouman.notevibe.presentation.theme.IosTextSecondaryLight
import com.nouman.notevibe.presentation.theme.SquircleLarge
import com.nouman.notevibe.presentation.theme.SquirclePill
import com.nouman.notevibe.presentation.viewmodel.NotesUiState

@Composable
fun FocusBoardScreen(
    uiState: NotesUiState,
    onNoteClick: (Note) -> Unit,
    onToggleCompletion: (Long) -> Unit,
    onUnpin: (Long) -> Unit,
    onNavigateToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val textPrimary = if (isDark) IosTextPrimaryDark else IosTextPrimaryLight
    val textSecondary = if (isDark) IosTextSecondaryDark else IosTextSecondaryLight

    val pinnedNotes = uiState.focusNotes

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item {
            IosTopBar(
                title = "Focus Board",
                subtitle = "Active Priorities (Max 3)",
                actions = {
                    // Slot Counter Pill
                    Box(
                        modifier = Modifier
                            .clip(SquirclePill)
                            .background(
                                if (uiState.focusStats.isFull) Color(0x26FF3B30) else Color(0x26007AFF)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${uiState.focusStats.pinnedCount}/3 Slots",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (uiState.focusStats.isFull) Color(0xFFFF3B30) else IosBlue
                        )
                    }
                }
            )
        }

        // Philosophy Info Card
        item {
            GlassmorphicCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                shape = SquircleLarge
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = IosBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Limiting yourself to exactly 3 daily focus items prevents cognitive overload and ensures peak momentum.",
                        style = MaterialTheme.typography.bodySmall,
                        color = textSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // 3 Focus Slots (Guaranteed slots 1, 2, and 3)
        items(3) { index ->
            val note = pinnedNotes.getOrNull(index)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                FocusSlotCard(
                    slotNumber = index + 1,
                    note = note,
                    onCardClick = { note?.let { onNoteClick(it) } },
                    onToggleCompletion = { note?.let { onToggleCompletion(it.id) } },
                    onUnpin = { note?.let { onUnpin(it.id) } },
                    onEmptySlotClick = onNavigateToCreate
                )
            }
        }
    }
}
