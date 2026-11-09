package com.nouman.notevibe.data.repository

import com.nouman.notevibe.data.local.dao.NoteDao
import com.nouman.notevibe.data.mapper.NoteMapper
import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.domain.model.Priority
import com.nouman.notevibe.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getActiveNotes(searchQuery: String, priorityFilter: Priority?): Flow<List<Note>> {
        val filterKey = priorityFilter?.key
        return noteDao.getActiveNotes(searchQuery.trim(), filterKey).map {
            NoteMapper.toDomainList(it)
        }
    }

    override fun getFocusBoardNotes(): Flow<List<Note>> {
        return noteDao.getFocusBoardNotes().map {
            NoteMapper.toDomainList(it)
        }
    }

    override fun getArchivedNotes(searchQuery: String): Flow<List<Note>> {
        return noteDao.getArchivedNotes(searchQuery.trim()).map {
            NoteMapper.toDomainList(it)
        }
    }

    override suspend fun getNoteById(id: Long): Note? {
        return noteDao.getNoteById(id)?.let { NoteMapper.toDomain(it) }
    }

    override suspend fun insertOrUpdateNote(note: Note): Long {
        val entity = NoteMapper.toEntity(note)
        return if (entity.id == 0L) {
            noteDao.insertNote(entity)
        } else {
            noteDao.updateNote(entity)
            entity.id
        }
    }

    override suspend fun deleteNote(noteId: Long) {
        noteDao.deleteNoteById(noteId)
    }

    override suspend fun togglePinToFocus(noteId: Long): Result<Boolean> {
        val entity = noteDao.getNoteById(noteId)
            ?: return Result.failure(NoSuchElementException("Note not found"))

        val targetPinned = !entity.isPinnedToFocus

        if (targetPinned) {
            val currentPinned = noteDao.getPinnedCount()
            if (currentPinned >= 3) {
                return Result.failure(IllegalStateException("Focus Board is full. You can only pin up to 3 notes."))
            }
        }

        val updated = entity.copy(
            isPinnedToFocus = targetPinned,
            updatedAt = System.currentTimeMillis()
        )
        noteDao.updateNote(updated)
        return Result.success(targetPinned)
    }

    override suspend fun toggleCompletion(noteId: Long): Boolean {
        val entity = noteDao.getNoteById(noteId) ?: return false
        val newStatus = !entity.isCompleted
        val completedAt = if (newStatus) System.currentTimeMillis() else null

        val updated = entity.copy(
            isCompleted = newStatus,
            completedAt = completedAt,
            // If completed, optionally keep or allow unpinning, but keep pinned status consistent with user choice
            updatedAt = System.currentTimeMillis()
        )
        noteDao.updateNote(updated)
        return newStatus
    }

    override suspend fun restoreNote(noteId: Long) {
        noteDao.restoreArchivedNote(noteId)
    }

    override suspend fun autoArchiveCompletedNotes(olderThanMillis: Long): Int {
        return noteDao.autoArchiveOldCompletedNotes(olderThanMillis)
    }

    override suspend fun getPinnedCount(): Int {
        return noteDao.getPinnedCount()
    }

    override suspend fun clearArchivedNotes() {
        noteDao.clearAllArchived()
    }
}
