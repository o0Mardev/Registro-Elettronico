package com.mardev.registroelettronico.feature_authentication.data.remote.dto.login

import com.mardev.registroelettronico.feature_authentication.domain.model.LoginInfo
import com.mardev.registroelettronico.feature_authentication.domain.model.Session
import com.mardev.registroelettronico.feature_authentication.domain.model.User

data class LoginInfoResponseDto(
    val errorcode: Int,
    val errormessage: String,
    val response: LoginInfoDataDto?
) {
    fun toLoginInfo(): LoginInfo {
        return LoginInfo(
            errorCode = errorcode,
            errorMessage = errormessage,
            if (response != null) {
                Session(
                    user = User(name = response.nome, surname = response.cognome),
                    userSessionId = response.usersession,
                    expiresAt = response.authExpire
                )
            } else null
        )
    }
}