package com.nouman.notevibe.domain.usecase

import com.nouman.notevibe.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Long) {
        repository.deleteNote(noteId)
    }

    suspend fun clearArchive() {
        repository.clearArchivedNotes()
    }
}
