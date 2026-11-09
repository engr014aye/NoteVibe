package com.nouman.notevibe.domain.repository

import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.domain.model.Priority
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getActiveNotes(searchQuery: String = "", priorityFilter: Priority? = null): Flow<List<Note>>
    fun getFocusBoardNotes(): Flow<List<Note>>
    fun getArchivedNotes(searchQuery: String = ""): Flow<List<Note>>
    suspend fun getNoteById(id: Long): Note?
    suspend fun insertOrUpdateNote(note: Note): Long
    suspend fun deleteNote(noteId: Long)
    suspend fun togglePinToFocus(noteId: Long): Result<Boolean>
    suspend fun toggleCompletion(noteId: Long): Boolean
    suspend fun restoreNote(noteId: Long)
    suspend fun autoArchiveCompletedNotes(olderThanMillis: Long): Int
    suspend fun getPinnedCount(): Int
    suspend fun clearArchivedNotes()
}
