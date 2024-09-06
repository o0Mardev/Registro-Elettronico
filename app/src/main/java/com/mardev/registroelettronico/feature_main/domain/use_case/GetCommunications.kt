package com.mardev.registroelettronico.feature_main.domain.use_case

import com.mardev.registroelettronico.core.data.remote.CommandJson
import com.mardev.registroelettronico.core.data.remote.JsonRequest
import com.mardev.registroelettronico.core.util.Constants
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_main.domain.model.Communication
import com.mardev.registroelettronico.feature_main.domain.repository.RetrieveDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetCommunications(
    private val repository: RetrieveDataRepository,
    private val sessionCache: SessionCache
) {
    suspend operator fun invoke(): Flow<Resource<List<Communication>>> {
        val taxCode = sessionCache.getTaxCode()
        val userSessionId = sessionCache.getActiveSession()?.userSessionId

        return if (taxCode!=null&&userSessionId!=null){
            val request = JsonRequest(
                sCodiceFiscale = taxCode,
                sSessionGuid = userSessionId,
                sCommandJSON = CommandJson(
                    sApplication = "FAM",
                    sService = "GET_COMUNICAZIONI_MASTER",

                    ),
                sVendorToken = Constants.vendorToken
            )
            repository.getAllCommunications(request).map { communicationResource ->
                when (communicationResource) {
                    is Resource.Success -> {

                        Resource.Success(
                            communicationResource.data?.filter {
                                it.studentId == sessionCache.getStudentId()
                            } ?: emptyList()
                        )
                    }

                    is Resource.Loading -> {
                        Resource.Loading(
                            communicationResource.data?.filter {
                                it.studentId == sessionCache.getStudentId()
                            }
                        )
                    }

                    is Resource.Error -> {
                        communicationResource
                    }
                }
            }
        } else flow {  }
    }
}