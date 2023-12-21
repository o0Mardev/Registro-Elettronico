package com.mardev.registroelettronico.feature_main.data.remote.dto.grades

data class GradeDataDto(
    val idAlunno: String,
    val idFrazione: String,
    val voti: List<GradeDto>
)