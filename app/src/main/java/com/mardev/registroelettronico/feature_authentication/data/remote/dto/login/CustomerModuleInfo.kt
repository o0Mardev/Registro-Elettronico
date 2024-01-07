package com.mardev.registroelettronico.feature_authentication.data.remote.dto.login

data class CustomerModuleInfo(
    val bAttivo: Boolean,
    val bCredential: Boolean,
    val dDataInizio: String,
    val dDataScadenza: String,
    val iId: Int,
    val iModule: Int,
    val iSpazioAcquistato: Int,
    val strModuleDescription: String,
    val strModuleKey: String,
    val strOthersSetting: String,
    val strPassword: String,
    val strServiceUrl: String,
    val strThirdCredential: String,
    val strUserName: String
)