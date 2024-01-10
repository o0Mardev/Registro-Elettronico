package com.mardev.registroelettronico.feature_authentication.domain.model

data class School(
    val taxCode: String,
    val type: String,
    val name: String,
    val region: String,
    val province: String,
    val city: String
)