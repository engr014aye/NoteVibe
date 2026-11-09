package com.nouman.notevibe.presentation.screens.editor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nouman.notevibe.domain.model.Priority
import com.nouman.notevibe.presentation.components.GlassmorphicCard
import com.nouman.notevibe.presentation.components.PriorityBadge
import com.nouman.notevibe.presentation.components.accentColor
import com.nouman.notevibe.presentation.components.springClickable
import com.nouman.notevibe.presentation.theme.IosBlue
import com.nouman.notevibe.presentation.theme.IosTextPrimaryDark
import com.nouman.notevibe.presentation.theme.IosTextPrimaryLight
import com.nouman.notevibe.presentation.theme.IosTextSecondaryDark
import com.nouman.notevibe.presentation.theme.IosTextSecondaryLight
import com.nouman.notevibe.presentation.theme.IosTextTertiaryLight
import com.nouman.notevibe.presentation.theme.SquircleLarge
import com.nouman.notevibe.presentation.theme.SquirclePill
import com.nouman.notevibe.presentation.viewmodel.EditorUiState

@Composable
fun NoteEditorScreen(
    uiState: EditorUiState,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    onPriorityChanged: (Priority) -> Unit,
    onTogglePin: (Boolean) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    onDismissError: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val textPrimary = if (isDark) IosTextPrimaryDark else IosTextPrimaryLight
    val textSecondary = if (isDark) IosTextSecondaryDark else IosTextSecondaryLight
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler {
        onBackClick()
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onBackClick()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            onDismissError()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .springClickable(onClick = onBackClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = textPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = if (uiState.isNewNote) "New Note" else "Edit Note",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )

                // Save Pill Button
                Button(
                    onClick = onSaveClick,
                    shape = SquirclePill,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IosBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.springClickable(onClick = onSaveClick)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Save",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Save",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Priority Selection Row
            Text(
                text = "PRIORITY",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = IosTextTertiaryLight,
                letterSpacing = 0.6.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Priority.entries.forEach { priority ->
                    val isSelected = uiState.priority == priority
                    val accent = priority.accentColor()

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(SquirclePill)
                            .background(
                                if (isSelected) accent.copy(alpha = 0.2f) else if (isDark) Color(0xFF1C1C1E) else Color(0xFFE5E5EA)
                            )
                            .then(
                                if (isSelected) Modifier.border(1.5.dp, accent, SquirclePill) else Modifier
                            )
                            .springClickable(onClick = { onPriorityChanged(priority) })
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        PriorityBadge(
                            priority = priority,
                            showLabel = true
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Pin to Focus Board Card
            GlassmorphicCard(
                modifier = Modifier.fillMaxWidth(),
                shape = SquircleLarge
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PushPin,
                            contentDescription = null,
                            tint = if (uiState.isPinnedToFocus) IosBlue else textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Pin to Focus (Top 3)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimary
                            )
                            Text(
                                text = if (uiState.focusBoardFull && !uiState.isPinnedToFocus)
                                    "Focus is full (3/3 slots occupied)"
                                else
                                    "Keep on your top daily dashboard",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 12.sp,
                                color = if (uiState.focusBoardFull && !uiState.isPinnedToFocus) Color(0xFFFF3B30) else textSecondary
                            )
                        }
                    }

                    Switch(
                        checked = uiState.isPinnedToFocus,
                        onCheckedChange = onTogglePin,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = IosBlue
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title Field (Apple Notes Large Headline)
            Box(modifier = Modifier.fillMaxWidth()) {
                if (uiState.title.isEmpty()) {
                    Text(
                        text = "Title",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = textSecondary.copy(alpha = 0.5f)
                    )
                }
                BasicTextField(
                    value = uiState.title,
                    onValueChange = onTitleChanged,
                    textStyle = MaterialTheme.typography.displayMedium.copy(
                        color = textPrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    cursorBrush = SolidColor(textPrimary),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Body Content Field (Apple Notes clean writing canvas)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 320.dp)
            ) {
                if (uiState.content.isEmpty()) {
                    Text(
                        text = "Note...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = textSecondary.copy(alpha = 0.5f),
                        lineHeight = 24.sp
                    )
                }
                BasicTextField(
                    value = uiState.content,
                    onValueChange = onContentChanged,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = textPrimary,
                        lineHeight = 24.sp
                    ),
                    cursorBrush = SolidColor(textPrimary),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}
