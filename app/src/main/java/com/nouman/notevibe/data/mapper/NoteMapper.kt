package com.nouman.notevibe.data.mapper

import com.nouman.notevibe.data.local.entity.NoteEntity
import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.domain.model.Priority

object NoteMapper {

    fun toDomain(entity: NoteEntity): Note {
        return Note(
            id = entity.id,
            title = entity.title,
            content = entity.content,
            priority = Priority.fromKey(entity.priority),
            isPinnedToFocus = entity.isPinnedToFocus,
            isCompleted = entity.isCompleted,
            completedAt = entity.completedAt,
            isArchived = entity.isArchived,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toEntity(domain: Note): NoteEntity {
        return NoteEntity(
            id = domain.id,
            title = domain.title,
            content = domain.content,
            priority = domain.priority.key,
            isPinnedToFocus = domain.isPinnedToFocus,
            isCompleted = domain.isCompleted,
            completedAt = domain.completedAt,
            isArchived = domain.isArchived,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }

    fun toDomainList(entities: List<NoteEntity>): List<Note> {
        return entities.map { toDomain(it) }
    }
}
