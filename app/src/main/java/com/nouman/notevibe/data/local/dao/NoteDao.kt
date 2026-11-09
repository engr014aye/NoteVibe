package com.nouman.notevibe.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nouman.notevibe.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("""
        SELECT * FROM notes 
        WHERE is_archived = 0 
          AND (:searchQuery = '' OR title LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%')
          AND (:priorityFilter IS NULL OR priority = :priorityFilter)
        ORDER BY is_pinned_to_focus DESC, updated_at DESC
    """)
    fun getActiveNotes(searchQuery: String, priorityFilter: String?): Flow<List<NoteEntity>>

    @Query("""
        SELECT * FROM notes 
        WHERE is_archived = 0 AND is_pinned_to_focus = 1
        ORDER BY updated_at DESC
        LIMIT 3
    """)
    fun getFocusBoardNotes(): Flow<List<NoteEntity>>

    @Query("""
        SELECT * FROM notes 
        WHERE is_archived = 1
          AND (:searchQuery = '' OR title LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%')
        ORDER BY updated_at DESC
    """)
    fun getArchivedNotes(searchQuery: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Query("SELECT COUNT(*) FROM notes WHERE is_archived = 0 AND is_pinned_to_focus = 1")
    suspend fun getPinnedCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNoteById(id: Long)

    @Query("DELETE FROM notes WHERE is_archived = 1")
    suspend fun clearAllArchived()

    /**
     * Moves completed notes older than the 7-day cutoff to the archive.
     */
    @Query("""
        UPDATE notes 
        SET is_archived = 1, is_pinned_to_focus = 0 
        WHERE is_completed = 1 
          AND is_archived = 0 
          AND completed_at IS NOT NULL 
          AND completed_at <= :cutoffTimestamp
    """)
    suspend fun autoArchiveOldCompletedNotes(cutoffTimestamp: Long): Int

    @Query("""
        UPDATE notes 
        SET is_archived = 0, is_completed = 0, completed_at = NULL, updated_at = :now 
        WHERE id = :id
    """)
    suspend fun restoreArchivedNote(id: Long, now: Long)
}
