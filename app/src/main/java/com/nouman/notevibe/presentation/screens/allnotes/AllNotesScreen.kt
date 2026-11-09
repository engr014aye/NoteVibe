package com.nouman.notevibe.presentation.screens.allnotes

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.domain.model.Priority
import com.nouman.notevibe.presentation.components.EmptyState
import com.nouman.notevibe.presentation.components.IosTopBar
import com.nouman.notevibe.presentation.components.NoteCard
import com.nouman.notevibe.presentation.components.PriorityFilterSegmentedControl
import com.nouman.notevibe.presentation.components.springClickable
import com.nouman.notevibe.presentation.theme.IosTextPrimaryDark
import com.nouman.notevibe.presentation.theme.IosTextPrimaryLight
import com.nouman.notevibe.presentation.theme.IosTextSecondaryDark
import com.nouman.notevibe.presentation.theme.IosTextSecondaryLight
import com.nouman.notevibe.presentation.theme.IosTextTertiaryLight
import com.nouman.notevibe.presentation.theme.SquircleMedium
import com.nouman.notevibe.presentation.theme.SquirclePill
import com.nouman.notevibe.presentation.viewmodel.NotesUiState

@Composable
fun AllNotesScreen(
    uiState: NotesUiState,
    onNoteClick: (Note) -> Unit,
    onToggleCompletion: (Long) -> Unit,
    onTogglePin: (Long) -> Unit,
    onDeleteNote: (Long) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onPriorityFilterChanged: (Priority?) -> Unit,
    onNavigateToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val textPrimary = if (isDark) IosTextPrimaryDark else IosTextPrimaryLight
    val textSecondary = if (isDark) IosTextSecondaryDark else IosTextSecondaryLight
    val searchBg = if (isDark) Color(0xFF1C1C1E) else Color(0xFFE5E5EA)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        item {
            IosTopBar(
                title = "All Notes",
                subtitle = "${uiState.allNotes.size} Active Notes"
            )
        }

        // Search Bar (Apple iOS Search Field)
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
                        contentDescription = "Search",
                        tint = textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (uiState.searchQuery.isEmpty()) {
                            Text(
                                text = "Search notes & tasks…",
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
                            contentDescription = "Clear",
                            tint = textSecondary,
                            modifier = Modifier
                                .size(18.dp)
                                .springClickable(onClick = { onSearchQueryChanged("") })
                        )
                    }
                }
            }
        }

        // Color-Coded Priority Filter Control
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                PriorityFilterSegmentedControl(
                    selectedPriority = uiState.priorityFilter,
                    onPrioritySelected = onPriorityFilterChanged
                )
            }
        }

        if (uiState.allNotes.isEmpty()) {
            item {
                EmptyState(
                    icon = Icons.Filled.Description,
                    title = if (uiState.searchQuery.isNotEmpty()) "No Matching Notes" else "No Notes Yet",
                    description = if (uiState.searchQuery.isNotEmpty())
                        "Try a different search keyword or switch priority filters."
                    else
                        "Capture your ideas, daily tasks, and focus goals with sleek Apple design.",
                    actionLabel = if (uiState.searchQuery.isNotEmpty()) "Clear Filter" else "Create Note",
                    onActionClick = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            onSearchQueryChanged("")
                            onPriorityFilterChanged(null)
                        } else {
                            onNavigateToCreate()
                        }
                    }
                )
            }
        } else {
            items(
                items = uiState.allNotes,
                key = { it.id }
            ) { note ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                ) {
                    NoteCard(
                        note = note,
                        onClick = { onNoteClick(note) },
                        onToggleCompletion = { onToggleCompletion(note.id) },
                        onTogglePin = { onTogglePin(note.id) },
                        onDelete = { onDeleteNote(note.id) }
                    )
                }
            }
        }
    }
}
