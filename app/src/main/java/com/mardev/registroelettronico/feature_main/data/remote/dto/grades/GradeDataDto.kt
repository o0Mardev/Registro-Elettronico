package com.mardev.registroelettronico.feature_main.common.data.remote.dto.grades

data class GradeDataDto(
    val idAlunno: String,
    val idFrazione: String,
    val voti: List<GradeDto>
)