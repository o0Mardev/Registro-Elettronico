package com.mardev.registroelettronico.feature_authentication.data.remote.dto.search

data class SchoolResponseDto(
    val errorcode: Int,
    val errormessage: String,
    val response: List<SchoolDataDto>?
)