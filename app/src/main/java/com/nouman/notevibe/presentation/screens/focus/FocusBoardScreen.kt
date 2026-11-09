package com.nouman.notevibe.presentation.screens.focus

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.presentation.components.FocusSlotCard
import com.nouman.notevibe.presentation.components.GlassmorphicCard
import com.nouman.notevibe.presentation.components.IosTopBar
import com.nouman.notevibe.presentation.components.PriorityBadge
import com.nouman.notevibe.presentation.components.springClickable
import com.nouman.notevibe.presentation.theme.IosBlue
import com.nouman.notevibe.presentation.theme.IosDarkSurface
import com.nouman.notevibe.presentation.theme.IosLightSurface
import com.nouman.notevibe.presentation.theme.IosTextPrimaryDark
import com.nouman.notevibe.presentation.theme.IosTextPrimaryLight
import com.nouman.notevibe.presentation.theme.IosTextSecondaryDark
import com.nouman.notevibe.presentation.theme.IosTextSecondaryLight
import com.nouman.notevibe.presentation.theme.PriorityLowColor
import com.nouman.notevibe.presentation.theme.SquircleLarge
import com.nouman.notevibe.presentation.theme.SquircleMedium
import com.nouman.notevibe.presentation.theme.SquirclePill
import com.nouman.notevibe.presentation.viewmodel.NotesUiState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusBoardScreen(
    uiState: NotesUiState,
    onNoteClick: (Note) -> Unit,
    onToggleCompletion: (Long) -> Unit,
    onUnpin: (Long) -> Unit,
    onPinExisting: (Long) -> Unit,
    onNavigateToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val textPrimary = if (isDark) IosTextPrimaryDark else IosTextPrimaryLight
    val textSecondary = if (isDark) IosTextSecondaryDark else IosTextSecondaryLight
    val sheetBg = if (isDark) IosDarkSurface else IosLightSurface

    val pinnedNotes = uiState.focusNotes
    val completedCount = pinnedNotes.count { it.isCompleted }
    val totalPinned = pinnedNotes.size
    val progress = if (totalPinned > 0) completedCount.toFloat() / totalPinned else 0f
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "focusProgress")

    val currentDateStr = remember {
        SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(Date())
    }

    val unpinnedNotes = remember(uiState.allNotes) {
        uiState.allNotes.filter { !it.isPinnedToFocus }
    }

    var slotToAssign by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item {
            IosTopBar(
                title = "Focus",
                subtitle = currentDateStr,
                actions = {
                    val statusText = when {
                        totalPinned == 0 -> "0/3 Active"
                        completedCount == totalPinned -> "All Done 🎉"
                        else -> "$completedCount of $totalPinned Done"
                    }
                    val badgeColor = when {
                        completedCount == totalPinned && totalPinned > 0 -> PriorityLowColor
                        uiState.focusStats.isFull -> IosBlue
                        else -> IosBlue
                    }

                    Box(
                        modifier = Modifier
                            .clip(SquirclePill)
                            .background(badgeColor.copy(alpha = 0.15f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = statusText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeColor
                        )
                    }
                }
            )
        }

        // Clean Progress Bar (Only shown if at least 1 note is pinned)
        if (totalPinned > 0) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(SquirclePill),
                        color = if (completedCount == totalPinned) PriorityLowColor else IosBlue,
                        trackColor = if (isDark) Color(0xFF2C2C2E) else Color(0xFFE5E5EA)
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
                    onEmptySlotClick = {
                        if (unpinnedNotes.isNotEmpty()) {
                            slotToAssign = index + 1
                        } else {
                            onNavigateToCreate()
                        }
                    }
                )
            }
        }
    }

    // Modal Sheet to assign an existing or new note to an empty focus slot
    slotToAssign?.let { slotNum ->
        ModalBottomSheet(
            onDismissRequest = { slotToAssign = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = sheetBg,
            shape = SquircleLarge
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Assign Slot #$slotNum",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )

                    Button(
                        onClick = {
                            slotToAssign = null
                            onNavigateToCreate()
                        },
                        shape = SquirclePill,
                        colors = ButtonDefaults.buttonColors(containerColor = IosBlue)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Note", fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (unpinnedNotes.isEmpty()) {
                    Text(
                        text = "No unpinned notes. Tap '+ New Note' to create one.",
                        style = MaterialTheme.typography.bodySmall,
                        color = textSecondary,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 280.dp)
                    ) {
                        items(unpinnedNotes, key = { it.id }) { unpinnedNote ->
                            GlassmorphicCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .springClickable {
                                        onPinExisting(unpinnedNote.id)
                                        slotToAssign = null
                                    },
                                shape = SquircleMedium
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (unpinnedNote.title.isNotBlank()) unpinnedNote.title else "Untitled Note",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = textPrimary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        if (unpinnedNote.content.isNotBlank()) {
                                            Text(
                                                text = unpinnedNote.content,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = textSecondary,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    PriorityBadge(priority = unpinnedNote.priority, showLabel = false)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
