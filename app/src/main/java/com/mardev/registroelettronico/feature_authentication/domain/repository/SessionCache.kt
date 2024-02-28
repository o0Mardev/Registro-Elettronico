package com.mardev.registroelettronico.feature_authentication.domain.repository

import com.mardev.registroelettronico.feature_authentication.domain.model.Session

interface SessionCache {

    suspend fun saveSession(session: Session)
    suspend fun saveTaxCode(taxCode: String)
    suspend fun saveStudentId(studentId: Int)
    suspend fun getStudentId(): Int?
    suspend fun getTaxCode(): String?
    suspend fun getActiveSession(): Session?
    suspend fun clearSession()

}