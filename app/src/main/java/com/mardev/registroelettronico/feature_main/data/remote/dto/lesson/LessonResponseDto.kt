package com.mardev.registroelettronico.feature_main.common.data.remote.dto.lesson

data class LessonResponseDto(
    val errorcode: Int,
    val errormessage: String,
    val response: List<LessonDataDto>?
)