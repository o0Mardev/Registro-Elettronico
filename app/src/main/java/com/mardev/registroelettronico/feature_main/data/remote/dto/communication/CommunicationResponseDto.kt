package com.mardev.registroelettronico.feature_main.common.data.remote.dto.communication

data class CommunicationResponseDto(
    val errorcode: Int,
    val errormessage: String,
    val response: List<CommunicationDataDto>?
)
