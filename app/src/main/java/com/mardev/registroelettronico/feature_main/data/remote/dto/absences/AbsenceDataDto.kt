package com.mardev.registroelettronico.feature_main.data.remote.dto.absences

data class AbsenceDataDto(
    val assenze: List<AbsenceDto>,
    val descFrazione: String,
    val idAlunno: String,
    val idFrazione: String
)