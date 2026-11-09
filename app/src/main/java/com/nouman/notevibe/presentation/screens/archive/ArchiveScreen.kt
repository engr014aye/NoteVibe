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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.presentation.components.EmptyState
import com.nouman.notevibe.presentation.components.IosTopBar
import com.nouman.notevibe.presentation.components.SwipeableNoteCard
import com.nouman.notevibe.presentation.components.springClickable
import com.nouman.notevibe.presentation.theme.IosBlue
import com.nouman.notevibe.presentation.theme.IosTextPrimaryDark
import com.nouman.notevibe.presentation.theme.IosTextPrimaryLight
import com.nouman.notevibe.presentation.theme.IosTextSecondaryDark
import com.nouman.notevibe.presentation.theme.IosTextSecondaryLight
import com.nouman.notevibe.presentation.theme.SquircleMedium
import com.nouman.notevibe.presentation.theme.SquirclePill
import com.nouman.notevibe.presentation.viewmodel.NotesUiState

@Composable
fun ArchiveScreen(
    uiState: NotesUiState,
    onNoteClick: (Note) -> Unit,
    onRestoreNote: (Long) -> Unit,
    onDeleteNote: (Long) -> Unit,
    onClearArchive: () -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val textPrimary = if (isDark) IosTextPrimaryDark else IosTextPrimaryLight
    val textSecondary = if (isDark) IosTextSecondaryDark else IosTextSecondaryLight
    val searchBg = if (isDark) Color(0xFF1C1C1E) else Color(0xFFE5E5EA)

    var showClearConfirmation by remember { mutableStateOf(false) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item {
            IosTopBar(
                title = "Archive",
                subtitle = "${uiState.archivedNotes.size} Archived (Auto-saved after 7d)",
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

        // Search Bar for Archive
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp)
                    .clip(SquirclePill)
                    .background(searchBg)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Archive",
                        tint = textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (uiState.searchQuery.isEmpty()) {
                            Text(
                                text = "Search archived notes…",
                                style = MaterialTheme.typography.bodyMedium,
                                color = textSecondary
                            )
                        }
                        BasicTextField(
                            value = uiState.searchQuery,
                            onValueChange = onSearchQueryChanged,
                            singleLine = true,
                            textStyle = MaterialTheme.typography.bodyMedium.copy(color = textPrimary),
                            cursorBrush = SolidColor(textPrimary),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (uiState.searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Clear Search",
                            tint = textSecondary,
                            modifier = Modifier
                                .size(18.dp)
                                .springClickable(onClick = { onSearchQueryChanged("") })
                        )
                    }
                }
            }
        }

        if (uiState.archivedNotes.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.Filled.Archive,
                    title = if (uiState.searchQuery.isNotEmpty()) "No Matching Notes" else "Archive is Empty",
                    description = if (uiState.searchQuery.isNotEmpty())
                        "No notes matching \"${uiState.searchQuery}\" were found."
                    else
                        "Completed notes automatically move here after 7 days.",
                    actionLabel = if (uiState.searchQuery.isNotEmpty()) "Clear Search" else null,
                    onActionClick = if (uiState.searchQuery.isNotEmpty()) { { onSearchQueryChanged("") } } else null
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
                    SwipeableNoteCard(
                        note = note,
                        onClick = { onNoteClick(note) },
                        onEdit = { onNoteClick(note) },
                        onDelete = { noteToDelete = note },
                        onToggleCompletion = { /* Archived items remain completed */ },
                        onRestore = { onRestoreNote(note.id) }
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
                    text = "This will permanently delete all archived notes.",
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

    noteToDelete?.let { note ->
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            title = {
                Text(
                    text = "Delete Note Permanently?",
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to permanently delete \"${if (note.title.isNotBlank()) note.title else "Untitled Note"}\"?",
                    color = textSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteNote(note.id)
                        noteToDelete = null
                    },
                    shape = SquirclePill,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF3B30))
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { noteToDelete = null }) {
                    Text("Cancel", color = IosBlue)
                }
            },
            shape = SquircleMedium
        )
    }
}
