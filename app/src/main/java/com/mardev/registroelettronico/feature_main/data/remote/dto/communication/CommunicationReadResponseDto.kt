package com.mardev.registroelettronico.feature_main.data.remote.dto.communication

data class CommunicationReadResponseDto(
    val errorcode: Int,
    val errormessage: String,
    val response: String
)