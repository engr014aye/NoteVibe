package com.nouman.notevibe.domain.usecase

import com.nouman.notevibe.domain.repository.NoteRepository

class RestoreArchivedNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long) {
        repository.restoreNote(noteId)
    }
}
