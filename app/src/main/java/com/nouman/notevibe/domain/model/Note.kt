package com.nouman.notevibe.domain.model

data class Note(
    val id: Long = 0,
    val title: String,
    val content: String,
    val priority: Priority = Priority.MEDIUM,
    val isPinnedToFocus: Boolean = false,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val isBlank: Boolean
        get() = title.isBlank() && content.isBlank()

    val daysUntilArchive: Int?
        get() {
            if (!isCompleted || completedAt == null || isArchived) return null
            val sevenDaysMillis = 7L * 24 * 60 * 60 * 1000
            val elapsed = System.currentTimeMillis() - completedAt
            val remainingMillis = sevenDaysMillis - elapsed
            return if (remainingMillis <= 0) 0 else (remainingMillis / (24 * 60 * 60 * 1000)).toInt() + 1
        }
}
