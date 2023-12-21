package com.mardev.registroelettronico.feature_main.data.remote.dto.grades

data class GradeResponseDto(
    val errorcode: Int,
    val errormessage: String,
    val response: List<GradeDataDto>?
)