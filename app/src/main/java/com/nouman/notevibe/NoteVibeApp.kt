package com.nouman.notevibe

import android.app.Application
import com.nouman.notevibe.data.local.database.NoteDatabase
import com.nouman.notevibe.data.repository.NoteRepositoryImpl
import com.nouman.notevibe.domain.repository.NoteRepository

class NoteVibeApp : Application() {

    lateinit var noteRepository: NoteRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val database = NoteDatabase.getInstance(this)
        noteRepository = NoteRepositoryImpl(database.noteDao)
    }
}
