package com.mardev.registroelettronico.feature_main.domain.use_case

import android.util.Log
import com.mardev.registroelettronico.core.data.remote.CommandJson
import com.mardev.registroelettronico.core.data.remote.JsonRequest
import com.mardev.registroelettronico.core.util.Constants
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_main.domain.model.Lesson
import com.mardev.registroelettronico.feature_main.domain.repository.RetrieveDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLessons(
    private val repository: RetrieveDataRepository,
    private val sessionCache: SessionCache
) {
    suspend operator fun invoke(): Flow<Resource<List<Lesson>>> {
        val taxCode = sessionCache.getTaxCode()
        val userSessionId = sessionCache.getActiveSession()?.userSessionId

        return if (taxCode!=null&&userSessionId!=null){
            val request = JsonRequest(
                sCodiceFiscale = taxCode,
                sSessionGuid = userSessionId,
                sCommandJSON = CommandJson(
                    sApplication = "FAM",
                    sService = "GET_ARGOMENTI_MASTER",
                ),
                sVendorToken = Constants.vendorToken
            )
            repository.getAllLessons(request, sessionCache.getStudentId())
        } else flow {  }
    }
}