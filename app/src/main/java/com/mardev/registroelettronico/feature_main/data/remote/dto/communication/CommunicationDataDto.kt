package com.mardev.registroelettronico.feature_main.common.data.remote.dto.communication

data class CommunicationDataDto(
    val idAlunno: String,
    val comunicazioni: List<CommunicationDto>
)
