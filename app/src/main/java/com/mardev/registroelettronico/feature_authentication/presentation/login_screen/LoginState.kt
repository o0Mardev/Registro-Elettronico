package com.mardev.registroelettronico.feature_authentication.presentation.login_screen

data class LoginState(
    val isLoading: Boolean = false,
    val taxCode: String = "",
    val userName: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isChecked: Boolean = false
)