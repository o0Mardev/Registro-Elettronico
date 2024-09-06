package com.mardev.registroelettronico.feature_main.presentation.components.note_screen

import com.mardev.registroelettronico.feature_main.domain.model.Note

data class NoteScreenState(
    val notes: List<Note> = emptyList(),
    val loading: Boolean = true
)