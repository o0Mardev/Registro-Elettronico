package com.mardev.registroelettronico.feature_main.data.remote.dto.structural

data class StructuralResponseDto(
    val errorcode: Int,
    val errormessage: String,
    val response: Response?
)