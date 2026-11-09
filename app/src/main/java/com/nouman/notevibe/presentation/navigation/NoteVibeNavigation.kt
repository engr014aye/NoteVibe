package com.nouman.notevibe.presentation.navigation

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nouman.notevibe.domain.repository.NoteRepository
import com.nouman.notevibe.presentation.screens.editor.NoteEditorScreen
import com.nouman.notevibe.presentation.screens.main.MainScreen
import com.nouman.notevibe.presentation.viewmodel.EditorViewModel
import com.nouman.notevibe.presentation.viewmodel.NotesViewModel
import com.nouman.notevibe.presentation.viewmodel.ViewModelFactory

@Composable
fun NoteVibeNavigation(
    navController: NavHostController,
    repository: NoteRepository
) {
    val factory = ViewModelFactory(repository)

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(
            route = Screen.Main.route,
            enterTransition = { fadeIn(spring(stiffness = Spring.StiffnessMedium)) },
            exitTransition = { fadeOut(spring(stiffness = Spring.StiffnessMedium)) }
        ) {
            val notesViewModel: NotesViewModel = viewModel(factory = factory)
            val uiState by notesViewModel.uiState.collectAsStateWithLifecycle()

            MainScreen(
                viewModel = notesViewModel,
                uiState = uiState,
                onNavigateToEditor = { noteId ->
                    navController.navigate(Screen.Editor.createRoute(noteId))
                }
            )
        }

        composable(
            route = Screen.Editor.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                    defaultValue = 0L
                }
            ),
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                ) + fadeIn()
            },
            exitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(stiffness = Spring.StiffnessMedium)
                ) + fadeOut()
            }
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
            val editorViewModel: EditorViewModel = viewModel(factory = factory)
            val uiState by editorViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(noteId) {
                editorViewModel.loadNote(noteId)
            }

            NoteEditorScreen(
                uiState = uiState,
                onTitleChanged = { editorViewModel.onTitleChanged(it) },
                onContentChanged = { editorViewModel.onContentChanged(it) },
                onPriorityChanged = { editorViewModel.onPriorityChanged(it) },
                onTogglePin = { editorViewModel.onTogglePin(it) },
                onSaveClick = { editorViewModel.saveNote() },
                onBackClick = { navController.popBackStack() },
                onDismissError = { editorViewModel.onDismissError() }
            )
        }
    }
}
