package com.mardev.registroelettronico.feature_main.domain.use_case

import com.mardev.registroelettronico.core.data.remote.CommandJson
import com.mardev.registroelettronico.core.data.remote.JsonRequest
import com.mardev.registroelettronico.core.domain.repository.DataStoreRepository
import com.mardev.registroelettronico.core.util.Constants
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_main.domain.model.Grade
import com.mardev.registroelettronico.feature_main.domain.repository.RetrieveDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class GetGrades @Inject constructor(
    private val repository: RetrieveDataRepository,
    private val sessionCache: SessionCache,
    @Named("userPreferencesDatastore") private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Grade>>> {

        val taxCode = sessionCache.getTaxCode()
        val userSessionId = sessionCache.getActiveSession()?.userSessionId

        return if (taxCode != null && userSessionId != null) {
            val request = JsonRequest(
                sCodiceFiscale = taxCode,
                sSessionGuid = userSessionId,
                sCommandJSON = CommandJson(
                    sApplication = "FAM",
                    sService = "GET_VOTI_LIST_DETAIL",

                    ),
                sVendorToken = Constants.vendorToken
            )
            repository.getAllGrades(request).map { gradeResource ->
                val selectedTimeFractionId =
                    dataStoreRepository.getInt("app_selected_time_fraction")
                when (gradeResource) {
                    is Resource.Success -> {

                        Resource.Success(
                            gradeResource.data?.filter {
                                it.studentId == sessionCache.getStudentId() &&
                                        it.idTimeFraction == selectedTimeFractionId
                            } ?: emptyList()
                        )
                    }

                    is Resource.Loading -> {
                        Resource.Loading(
                            gradeResource.data?.filter {
                                it.studentId == sessionCache.getStudentId() &&
                                        it.idTimeFraction == selectedTimeFractionId
                            }
                        )
                    }

                    is Resource.Error -> {
                        gradeResource
                    }
                }
            }
        } else flow { }
    }
}