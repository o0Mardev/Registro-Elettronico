package com.mardev.registroelettronico.feature_main.domain.use_case

import android.util.Log
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_main.domain.model.Homework
import com.mardev.registroelettronico.feature_main.domain.repository.RetrieveDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetHomework(
    private val repository: RetrieveDataRepository,
    private val sessionCache: SessionCache
) {

    suspend operator fun invoke(): Flow<Resource<List<Homework>>> {
        Log.d("TAG", "invoke: GetHomework")

        val taxCode = sessionCache.getTaxCode()
        val userSessionId = sessionCache.getActiveSession()?.userSessionId


        return if (taxCode!=null&&userSessionId!=null){
            repository.getAllHomework(taxCode, userSessionId)
        } else flow {  }
    }

}