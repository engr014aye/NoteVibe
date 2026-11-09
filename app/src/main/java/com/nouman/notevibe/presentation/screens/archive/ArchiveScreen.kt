package com.nouman.notevibe.presentation.screens.archive

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nouman.notevibe.presentation.components.EmptyState
import com.nouman.notevibe.presentation.components.GlassmorphicCard
import com.nouman.notevibe.presentation.components.IosTopBar
import com.nouman.notevibe.presentation.components.NoteCard
import com.nouman.notevibe.presentation.components.springClickable
import com.nouman.notevibe.presentation.theme.IosBlue
import com.nouman.notevibe.presentation.theme.IosTextPrimaryDark
import com.nouman.notevibe.presentation.theme.IosTextPrimaryLight
import com.nouman.notevibe.presentation.theme.IosTextSecondaryDark
import com.nouman.notevibe.presentation.theme.IosTextSecondaryLight
import com.nouman.notevibe.presentation.theme.SquircleLarge
import com.nouman.notevibe.presentation.theme.SquircleMedium
import com.nouman.notevibe.presentation.theme.SquirclePill
import com.nouman.notevibe.presentation.viewmodel.NotesUiState

@Composable
fun ArchiveScreen(
    uiState: NotesUiState,
    onRestoreNote: (Long) -> Unit,
    onDeleteNote: (Long) -> Unit,
    onClearArchive: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val textPrimary = if (isDark) IosTextPrimaryDark else IosTextPrimaryLight
    val textSecondary = if (isDark) IosTextSecondaryDark else IosTextSecondaryLight

    var showClearConfirmation by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item {
            IosTopBar(
                title = "Archive",
                subtitle = "${uiState.archivedNotes.size} Archived Items",
                actions = {
                    if (uiState.archivedNotes.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .clip(SquirclePill)
                                .background(Color(0x1FFF3B30))
                                .springClickable(onClick = { showClearConfirmation = true })
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.DeleteSweep,
                                    contentDescription = "Clear Archive",
                                    tint = Color(0xFFFF3B30),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Clear All",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFFF3B30)
                                )
                            }
                        }
                    }
                }
            )
        }

        // 7-Day Auto Archive Info Card
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
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = IosBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Automated Clean Workspace: Completed notes automatically move here after 7 days. You can restore any note back to active at any time.",
                        style = MaterialTheme.typography.bodySmall,
                        color = textSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        if (uiState.archivedNotes.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.Filled.Archive,
                    title = "Archive is Empty",
                    description = "When you complete notes, NoteVibe automatically retires them here after 7 days to preserve your clutter-free focus."
                )
            }
        } else {
            items(
                items = uiState.archivedNotes,
                key = { it.id }
            ) { note ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    NoteCard(
                        note = note,
                        onClick = { /* View only in archive */ },
                        onToggleCompletion = { /* Completed */ },
                        onRestore = { onRestoreNote(note.id) },
                        onDelete = { onDeleteNote(note.id) }
                    )
                }
            }
        }
    }

    if (showClearConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearConfirmation = false },
            title = {
                Text(
                    text = "Clear Entire Archive?",
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
            },
            text = {
                Text(
                    text = "This will permanently delete all archived notes stored in your local Room database.",
                    color = textSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onClearArchive()
                        showClearConfirmation = false
                    },
                    shape = SquirclePill,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30))
                ) {
                    Text("Delete All", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmation = false }) {
                    Text("Cancel", color = IosBlue)
                }
            },
            shape = SquircleMedium
        )
    }
}
