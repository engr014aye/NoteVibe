package com.nouman.notevibe.domain.usecase

import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.domain.repository.NoteRepository

class SaveNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Result<Long> {
        if (note.isBlank) {
            return Result.failure(IllegalArgumentException("Note cannot be empty."))
        }

        // If the note is set as pinned, ensure slot limit is respected
        if (note.isPinnedToFocus) {
            val currentPinned = repository.getPinnedCount()
            val existing = if (note.id != 0L) repository.getNoteById(note.id) else null
            val wasAlreadyPinned = existing?.isPinnedToFocus == true

            if (!wasAlreadyPinned && currentPinned >= 3) {
                return Result.failure(IllegalStateException("Focus Board is full. Max 3 notes can be pinned."))
            }
        }

        val updatedNote = note.copy(
            updatedAt = System.currentTimeMillis()
        )
        val id = repository.insertOrUpdateNote(updatedNote)
        return Result.success(id)
    }
}
