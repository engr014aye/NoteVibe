package com.nouman.notevibe.presentation.viewmodel

import com.nouman.notevibe.domain.model.FocusBoardStats
import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.domain.model.Priority
import com.nouman.notevibe.presentation.components.BottomTab

data class NotesUiState(
    val focusNotes: List<Note> = emptyList(),
    val allNotes: List<Note> = emptyList(),
    val archivedNotes: List<Note> = emptyList(),
    val searchQuery: String = "",
    val priorityFilter: Priority? = null,
    val currentTab: BottomTab = BottomTab.FOCUS,
    val focusStats: FocusBoardStats = FocusBoardStats(),
    val userMessage: String? = null,
    val isLoading: Boolean = false
)
