package com.mardev.registroelettronico.feature_main.data.remote.dto.absences

data class AbsenceResponseDto(
    val errorcode: Int,
    val errormessage: String,
    val response: List<AbsenceDataDto>?
)