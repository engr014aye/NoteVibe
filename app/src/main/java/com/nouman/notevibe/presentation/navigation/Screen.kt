package com.nouman.notevibe.presentation.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("main_screen")
    data object Editor : Screen("editor_screen/{noteId}") {
        fun createRoute(noteId: Long = 0L): String = "editor_screen/$noteId"
    }
}
