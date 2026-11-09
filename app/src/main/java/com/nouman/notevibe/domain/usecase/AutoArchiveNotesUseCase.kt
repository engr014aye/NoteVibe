package com.nouman.notevibe.domain.usecase

import com.nouman.notevibe.domain.repository.NoteRepository

class AutoArchiveNotesUseCase(
    private val repository: NoteRepository
) {
    /**
     * Finds all completed notes older than 7 days (7 * 24 * 60 * 60 * 1000 ms)
     * and sets isArchived = true and isPinnedToFocus = false.
     * Returns the count of notes automatically moved to archive.
     */
    suspend operator fun invoke(): Int {
        val sevenDaysMillis = 7L * 24 * 60 * 60 * 1000
        val cutoffTimestamp = System.currentTimeMillis() - sevenDaysMillis
        return repository.autoArchiveCompletedNotes(cutoffTimestamp)
    }
}
