package com.nouman.notevibe.domain.usecase

import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.domain.repository.NoteRepository

class GetNoteByIdUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Long): Note? {
        return repository.getNoteById(id)
    }
}
