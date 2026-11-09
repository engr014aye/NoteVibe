package com.nouman.notevibe.domain.model

data class FocusBoardStats(
    val pinnedCount: Int = 0,
    val maxSlots: Int = 3
) {
    val isFull: Boolean
        get() = pinnedCount >= maxSlots

    val availableSlots: Int
        get() = (maxSlots - pinnedCount).coerceAtLeast(0)

    val label: String
        get() = "$pinnedCount of $maxSlots Slots Occupied"
}
