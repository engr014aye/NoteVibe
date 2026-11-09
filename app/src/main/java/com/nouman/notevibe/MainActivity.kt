package com.nouman.notevibe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.nouman.notevibe.presentation.navigation.NoteVibeNavigation
import com.nouman.notevibe.presentation.theme.NoteVibeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as NoteVibeApp
        val repository = app.noteRepository

        setContent {
            NoteVibeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NoteVibeNavigation(
                        navController = navController,
                        repository = repository
                    )
                }
            }
        }
    }
}
