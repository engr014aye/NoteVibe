package com.nouman.notevibe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.domain.model.Priority
import com.nouman.notevibe.domain.repository.NoteRepository
import com.nouman.notevibe.domain.usecase.GetNoteByIdUseCase
import com.nouman.notevibe.domain.usecase.SaveNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditorViewModel(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val repository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    fun loadNote(noteId: Long) {
        viewModelScope.launch {
            val pinnedCount = repository.getPinnedCount()
            val isFull = pinnedCount >= 3

            if (noteId != 0L) {
                val existingNote = getNoteByIdUseCase(noteId)
                if (existingNote != null) {
                    _uiState.update {
                        it.copy(
                            id = existingNote.id,
                            title = existingNote.title,
                            content = existingNote.content,
                            priority = existingNote.priority,
                            isPinnedToFocus = existingNote.isPinnedToFocus,
                            isCompleted = existingNote.isCompleted,
                            isNewNote = false,
                            focusBoardFull = isFull && !existingNote.isPinnedToFocus
                        )
                    }
                    return@launch
                }
            }

            _uiState.update {
                it.copy(
                    isNewNote = true,
                    focusBoardFull = isFull
                )
            }
        }
    }

    fun onTitleChanged(title: String) {
        _uiState.update { it.copy(title = title, errorMessage = null) }
    }

    fun onContentChanged(content: String) {
        _uiState.update { it.copy(content = content, errorMessage = null) }
    }

    fun onPriorityChanged(priority: Priority) {
        _uiState.update { it.copy(priority = priority) }
    }

    fun onTogglePin(pinned: Boolean) {
        if (pinned && _uiState.value.focusBoardFull && !_uiState.value.isPinnedToFocus) {
            _uiState.update {
                it.copy(errorMessage = "Focus Board is full (Max 3 notes). Unpin one to add another.")
            }
            return
        }
        _uiState.update { it.copy(isPinnedToFocus = pinned, errorMessage = null) }
    }

    fun saveNote() {
        val state = _uiState.value
        if (state.title.isBlank() && state.content.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter a title or note body.") }
            return
        }

        viewModelScope.launch {
            val note = Note(
                id = state.id,
                title = state.title.trim(),
                content = state.content.trim(),
                priority = state.priority,
                isPinnedToFocus = state.isPinnedToFocus,
                isCompleted = state.isCompleted
            )

            val result = saveNoteUseCase(note)
            result.onSuccess {
                _uiState.update { it.copy(isSaved = true) }
            }.onFailure { error ->
                _uiState.update { it.copy(errorMessage = error.message ?: "Failed to save note.") }
            }
        }
    }

    fun onDismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
