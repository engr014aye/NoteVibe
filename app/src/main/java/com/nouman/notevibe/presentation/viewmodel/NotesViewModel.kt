package com.nouman.notevibe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nouman.notevibe.domain.model.FocusBoardStats
import com.nouman.notevibe.domain.model.Priority
import com.nouman.notevibe.domain.usecase.AutoArchiveNotesUseCase
import com.nouman.notevibe.domain.usecase.DeleteNoteUseCase
import com.nouman.notevibe.domain.usecase.GetArchivedNotesUseCase
import com.nouman.notevibe.domain.usecase.GetFocusNotesUseCase
import com.nouman.notevibe.domain.usecase.GetNotesUseCase
import com.nouman.notevibe.domain.usecase.RestoreArchivedNoteUseCase
import com.nouman.notevibe.domain.usecase.ToggleCompletionUseCase
import com.nouman.notevibe.domain.usecase.ToggleFocusPinUseCase
import com.nouman.notevibe.presentation.components.BottomTab
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel(
    private val getNotesUseCase: GetNotesUseCase,
    private val getFocusNotesUseCase: GetFocusNotesUseCase,
    private val getArchivedNotesUseCase: GetArchivedNotesUseCase,
    private val toggleFocusPinUseCase: ToggleFocusPinUseCase,
    private val toggleCompletionUseCase: ToggleCompletionUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val autoArchiveNotesUseCase: AutoArchiveNotesUseCase,
    private val restoreArchivedNoteUseCase: RestoreArchivedNoteUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _priorityFilter = MutableStateFlow<Priority?>(null)
    private val _currentTab = MutableStateFlow(BottomTab.FOCUS)
    private val _userMessage = MutableStateFlow<String?>(null)

    // Flow for Focus Board Notes (strictly max 3)
    private val _focusNotes = getFocusNotesUseCase()

    // Flow for Active Notes with dynamic search & priority filter
    private val _activeNotes = combine(_searchQuery, _priorityFilter) { query, priority ->
        Pair(query, priority)
    }.flatMapLatest { (query, priority) ->
        getNotesUseCase(query, priority)
    }

    // Flow for Archived Notes
    private val _archivedNotes = _searchQuery.flatMapLatest { query ->
        getArchivedNotesUseCase(query)
    }

    val uiState: StateFlow<NotesUiState> = combine(
        _focusNotes,
        _activeNotes,
        _archivedNotes,
        _searchQuery,
        _priorityFilter,
        _currentTab,
        _userMessage
    ) { focusNotes, activeNotes, archivedNotes, searchQuery, priorityFilter, currentTab, userMessage ->
        NotesUiState(
            focusNotes = focusNotes,
            allNotes = activeNotes,
            archivedNotes = archivedNotes,
            searchQuery = searchQuery,
            priorityFilter = priorityFilter,
            currentTab = currentTab,
            focusStats = FocusBoardStats(pinnedCount = focusNotes.size),
            userMessage = userMessage
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotesUiState()
    )

    init {
        // Automatically run 7-day auto-archive on startup
        runAutoArchive()
    }

    fun onTabSelected(tab: BottomTab) {
        _currentTab.update { tab }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.update { query }
    }

    fun onPriorityFilterChanged(priority: Priority?) {
        _priorityFilter.update { priority }
    }

    fun onTogglePin(noteId: Long) {
        viewModelScope.launch {
            val result = toggleFocusPinUseCase(noteId)
            result.onFailure { error ->
                _userMessage.update { error.message ?: "Cannot pin note." }
            }
        }
    }

    fun onToggleCompletion(noteId: Long) {
        viewModelScope.launch {
            toggleCompletionUseCase(noteId)
        }
    }

    fun onDeleteNote(noteId: Long) {
        viewModelScope.launch {
            deleteNoteUseCase(noteId)
        }
    }

    fun onRestoreNote(noteId: Long) {
        viewModelScope.launch {
            restoreArchivedNoteUseCase(noteId)
            _userMessage.update { "Note restored to active notes." }
        }
    }

    fun onClearArchive() {
        viewModelScope.launch {
            deleteNoteUseCase.clearArchive()
            _userMessage.update { "Archive cleared." }
        }
    }

    fun onDismissUserMessage() {
        _userMessage.update { null }
    }

    private fun runAutoArchive() {
        viewModelScope.launch {
            val archivedCount = autoArchiveNotesUseCase()
            if (archivedCount > 0) {
                _userMessage.update { "$archivedCount completed note(s) moved to Archive (7-day rule)." }
            }
        }
    }
}
