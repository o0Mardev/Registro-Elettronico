package com.mardev.registroelettronico.feature_main.data.remote.dto.homework

data class HomeworkResponseDto(
    val errorcode: Int,
    val errormessage: String,
    val response: List<HomeworkDataDto>?
)