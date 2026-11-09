package com.nouman.notevibe.presentation.viewmodel

import com.nouman.notevibe.domain.model.Priority

data class EditorUiState(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val priority: Priority = Priority.MEDIUM,
    val isPinnedToFocus: Boolean = false,
    val isCompleted: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
    val isNewNote: Boolean = true,
    val focusBoardFull: Boolean = false
)
