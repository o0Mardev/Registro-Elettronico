package com.mardev.registroelettronico.feature_authentication.domain.model

data class Session(
    val user: User,
    val userSessionId: String,
    val expiresAt: String,
)

data class User(
    val name: String,
    val surname: String
)
