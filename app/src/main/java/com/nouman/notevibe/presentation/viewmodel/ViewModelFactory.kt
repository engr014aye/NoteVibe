package com.nouman.notevibe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nouman.notevibe.domain.repository.NoteRepository
import com.nouman.notevibe.domain.usecase.AutoArchiveNotesUseCase
import com.nouman.notevibe.domain.usecase.DeleteNoteUseCase
import com.nouman.notevibe.domain.usecase.GetArchivedNotesUseCase
import com.nouman.notevibe.domain.usecase.GetFocusNotesUseCase
import com.nouman.notevibe.domain.usecase.GetNoteByIdUseCase
import com.nouman.notevibe.domain.usecase.GetNotesUseCase
import com.nouman.notevibe.domain.usecase.RestoreArchivedNoteUseCase
import com.nouman.notevibe.domain.usecase.SaveNoteUseCase
import com.nouman.notevibe.domain.usecase.ToggleCompletionUseCase
import com.nouman.notevibe.domain.usecase.ToggleFocusPinUseCase

class ViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NotesViewModel::class.java) -> {
                NotesViewModel(
                    getNotesUseCase = GetNotesUseCase(repository),
                    getFocusNotesUseCase = GetFocusNotesUseCase(repository),
                    getArchivedNotesUseCase = GetArchivedNotesUseCase(repository),
                    toggleFocusPinUseCase = ToggleFocusPinUseCase(repository),
                    toggleCompletionUseCase = ToggleCompletionUseCase(repository),
                    deleteNoteUseCase = DeleteNoteUseCase(repository),
                    autoArchiveNotesUseCase = AutoArchiveNotesUseCase(repository),
                    restoreArchivedNoteUseCase = RestoreArchivedNoteUseCase(repository)
                ) as T
            }
            modelClass.isAssignableFrom(EditorViewModel::class.java) -> {
                EditorViewModel(
                    getNoteByIdUseCase = GetNoteByIdUseCase(repository),
                    saveNoteUseCase = SaveNoteUseCase(repository),
                    repository = repository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
