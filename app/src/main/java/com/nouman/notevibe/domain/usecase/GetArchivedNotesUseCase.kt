package com.nouman.notevibe.domain.usecase

import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetArchivedNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(searchQuery: String = ""): Flow<List<Note>> {
        return repository.getArchivedNotes(searchQuery)
    }
}
