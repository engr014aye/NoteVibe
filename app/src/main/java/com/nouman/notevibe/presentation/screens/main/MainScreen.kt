package com.nouman.notevibe.presentation.screens.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nouman.notevibe.domain.model.Note
import com.nouman.notevibe.presentation.components.BottomTab
import com.nouman.notevibe.presentation.components.IosFloatingBottomBar
import com.nouman.notevibe.presentation.screens.allnotes.AllNotesScreen
import com.nouman.notevibe.presentation.screens.archive.ArchiveScreen
import com.nouman.notevibe.presentation.screens.focus.FocusBoardScreen
import com.nouman.notevibe.presentation.viewmodel.NotesUiState
import com.nouman.notevibe.presentation.viewmodel.NotesViewModel

@Composable
fun MainScreen(
    viewModel: NotesViewModel,
    uiState: NotesUiState,
    onNavigateToEditor: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.onDismissUserMessage()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            AnimatedContent(
                targetState = uiState.currentTab,
                transitionSpec = {
                    fadeIn(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) togetherWith fadeOut(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                },
                label = "tabTransition"
            ) { tab ->
                when (tab) {
                    BottomTab.FOCUS -> {
                        FocusBoardScreen(
                            uiState = uiState,
                            onNoteClick = { onNavigateToEditor(it.id) },
                            onToggleCompletion = { viewModel.onToggleCompletion(it) },
                            onUnpin = { viewModel.onTogglePin(it) },
                            onNavigateToCreate = { onNavigateToEditor(0L) }
                        )
                    }
                    BottomTab.NOTES -> {
                        AllNotesScreen(
                            uiState = uiState,
                            onNoteClick = { onNavigateToEditor(it.id) },
                            onToggleCompletion = { viewModel.onToggleCompletion(it) },
                            onTogglePin = { viewModel.onTogglePin(it) },
                            onDeleteNote = { viewModel.onDeleteNote(it) },
                            onSearchQueryChanged = { viewModel.onSearchQueryChanged(it) },
                            onPriorityFilterChanged = { viewModel.onPriorityFilterChanged(it) },
                            onNavigateToCreate = { onNavigateToEditor(0L) }
                        )
                    }
                    BottomTab.ARCHIVE -> {
                        ArchiveScreen(
                            uiState = uiState,
                            onRestoreNote = { viewModel.onRestoreNote(it) },
                            onDeleteNote = { viewModel.onDeleteNote(it) },
                            onClearArchive = { viewModel.onClearArchive() }
                        )
                    }
                }
            }
        }

        // Floating Translucent Bottom Bar
        IosFloatingBottomBar(
            currentTab = uiState.currentTab,
            onTabSelected = { viewModel.onTabSelected(it) },
            onAddNoteClick = { onNavigateToEditor(0L) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Snackbar Host for Alerts
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 90.dp)
        )
    }
}
