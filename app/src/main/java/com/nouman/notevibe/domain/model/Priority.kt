package com.nouman.notevibe.domain.model

enum class Priority(
    val key: String,
    val displayName: String,
    val rank: Int
) {
    HIGH(
        key = "high",
        displayName = "High Priority",
        rank = 1
    ),
    MEDIUM(
        key = "medium",
        displayName = "Medium Priority",
        rank = 2
    ),
    LOW(
        key = "low",
        displayName = "Low Priority",
        rank = 3
    );

    companion object {
        fun fromKey(key: String?): Priority {
            return entries.firstOrNull { it.key.equals(key, ignoreCase = true) } ?: MEDIUM
        }
    }
}
