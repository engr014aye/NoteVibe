package com.nouman.notevibe.domain.usecase

import com.nouman.notevibe.domain.repository.NoteRepository

class ToggleCompletionUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long): Boolean {
        return repository.toggleCompletion(noteId)
    }
}
