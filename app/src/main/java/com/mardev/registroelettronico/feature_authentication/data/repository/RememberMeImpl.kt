package com.mardev.registroelettronico.feature_authentication.data.repository

import com.mardev.registroelettronico.core.domain.repository.DataStoreRepository
import com.mardev.registroelettronico.feature_authentication.domain.repository.RememberMe
import javax.inject.Inject

class RememberMeImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : RememberMe {

    override suspend fun saveTaxcode(taxCode: String) {
        dataStoreRepository.putString("taxCode", taxCode)
    }

    override suspend fun saveUsername(username: String) {
        dataStoreRepository.putString("username", username)
    }

    override suspend fun savePassword(password: String) {
        dataStoreRepository.putString("password", password)
    }

    override suspend fun saveCheckboxState(state: Boolean) {
        dataStoreRepository.putBoolean("checkBoxState", state)
    }


    override suspend fun getTaxCode(): String? {
        return dataStoreRepository.getString("taxCode")
    }

    override suspend fun getUsername(): String? {
        return dataStoreRepository.getString("username")
    }

    override suspend fun getPassword(): String? {
        return dataStoreRepository.getString("password")
    }

    override suspend fun getCheckboxState(): Boolean? {
        return dataStoreRepository.getBoolean("checkBoxState")
    }
}