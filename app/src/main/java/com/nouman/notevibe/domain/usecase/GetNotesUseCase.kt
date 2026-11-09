package com.nouman.notevibe.domain.usecase

import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.domain.model.Priority
import com.nouman.notevibe.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(searchQuery: String = "", priorityFilter: Priority? = null): Flow<List<Note>> {
        return repository.getActiveNotes(searchQuery, priorityFilter)
    }
}
