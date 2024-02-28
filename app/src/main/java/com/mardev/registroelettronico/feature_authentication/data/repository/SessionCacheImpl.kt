package com.mardev.registroelettronico.feature_authentication.data.repository

import android.util.Log
import com.google.gson.GsonBuilder
import com.mardev.registroelettronico.core.domain.repository.DataStoreRepository
import com.mardev.registroelettronico.feature_authentication.domain.model.Session
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import javax.inject.Inject

class SessionCacheImpl @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : SessionCache {

    private val gson = GsonBuilder().create()

    override suspend fun saveSession(session: Session) {
        Log.d("TAG", "saveSession: $session")
        dataStoreRepository.putString(
            "userSession", gson
                .toJson(session)
        )
    }

    override suspend fun saveTaxCode(taxCode: String) {
        dataStoreRepository.putString("taxCode", taxCode)
    }

    override suspend fun saveStudentId(studentId: Int) {
        dataStoreRepository.putInt("studentId", studentId)
    }

    override suspend fun getStudentId(): Int? {
        return dataStoreRepository.getInt("studentId")
    }

    override suspend fun getTaxCode(): String? {
        return dataStoreRepository.getString("taxCode")
    }

    override suspend fun getActiveSession(): Session? {
        val json = dataStoreRepository.getString("userSession")
        return gson.fromJson(json, Session::class.java)
    }

    override suspend fun clearSession() {
        dataStoreRepository.putString("userSession", "")
    }


}