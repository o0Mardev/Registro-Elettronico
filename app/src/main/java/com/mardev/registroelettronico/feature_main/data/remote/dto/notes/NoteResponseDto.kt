package com.mardev.registroelettronico.feature_main.data.remote.dto.notes

data class NoteResponseDto(
    val errorcode: Int,
    val errormessage: String?,
    val response: List<NoteDataDto>?
)