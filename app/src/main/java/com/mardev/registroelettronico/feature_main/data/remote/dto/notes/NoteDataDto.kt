package com.mardev.registroelettronico.feature_main.data.remote.dto.notes

data class NoteDataDto(
    val descFrazione: String,
    val idAlunno: String,
    val idFrazione: String,
    val note: List<NoteDto>
)