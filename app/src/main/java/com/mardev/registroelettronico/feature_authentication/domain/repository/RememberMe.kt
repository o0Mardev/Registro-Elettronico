package com.mardev.registroelettronico.feature_authentication.domain.repository

interface RememberMe {
    suspend fun saveTaxcode(taxCode: String)

    suspend fun saveUsername(username: String)

    suspend fun savePassword(password: String)

    suspend fun saveCheckboxState(state: Boolean)


    suspend fun getTaxCode(): String?

    suspend fun getUsername(): String?

    suspend fun getPassword(): String?

    suspend fun getCheckboxState(): Boolean?
}