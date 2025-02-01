package com.mardev.registroelettronico.feature_authentication.domain.repository

interface RememberMe {
    suspend fun saveTaxCode(taxCode: String)

    suspend fun saveUsername(username: String)

    suspend fun savePassword(password: String)


    suspend fun getTaxCode(): String?

    suspend fun getUsername(): String?

    suspend fun getPassword(): String?

}