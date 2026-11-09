package com.nouman.notevibe.domain.usecase

import com.nouman.notevibe.domain.repository.NoteRepository

class ToggleFocusPinUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long): Result<Boolean> {
        return repository.togglePinToFocus(noteId)
    }
}
