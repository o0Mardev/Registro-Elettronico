package com.mardev.registroelettronico.feature_main.common.domain.use_case

import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_main.common.domain.model.Communication
import com.mardev.registroelettronico.feature_main.common.domain.repository.RetrieveDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCommunications(
    private val repository: RetrieveDataRepository,
    private val sessionCache: SessionCache
) {
    suspend operator fun invoke(): Flow<Resource<List<Communication>>> {
        val taxCode = sessionCache.getTaxCode()
        val userSessionId = sessionCache.getActiveSession()?.userSessionId

        return if (taxCode!=null&&userSessionId!=null){
            repository.getAllCommunications(taxCode, userSessionId)
        } else flow {  }
    }
}