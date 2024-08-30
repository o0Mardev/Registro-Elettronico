package com.mardev.registroelettronico.feature_main.presentation.components.note_screen

import com.mardev.registroelettronico.feature_main.domain.model.Note

data class NoteScreenState(
    val allNotes: List<Note> = emptyList(),
    val filteredNotes: List<Note> = emptyList(),
    val loading: Boolean = true
)