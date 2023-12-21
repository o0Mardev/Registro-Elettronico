package com.mardev.registroelettronico.feature_authentication.domain.model

data class LoginInfo(
    val errorCode: Int,
    val errorMessage: String,
    val session: Session?
)
