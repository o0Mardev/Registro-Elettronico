package com.mardev.registroelettronico.feature_main.domain.use_case

import com.mardev.registroelettronico.core.data.remote.CommandJson
import com.mardev.registroelettronico.core.data.remote.Data
import com.mardev.registroelettronico.core.data.remote.JsonRequest
import com.mardev.registroelettronico.core.domain.repository.DataStoreRepository
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_main.domain.model.Note
import com.mardev.registroelettronico.feature_main.domain.repository.RetrieveDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class GetNotes @Inject constructor(
    private val repository: RetrieveDataRepository,
    private val sessionCache: SessionCache,
    @Named("userPreferencesDatastore") private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Note>>> {
        val taxCode = sessionCache.getTaxCode()
        val userSessionId = sessionCache.getActiveSession()?.userSessionId
        val studentId = sessionCache.getStudentId()

        return if (taxCode != null && userSessionId != null && studentId != null) {
            val request = JsonRequest(
                sCodiceFiscale = taxCode,
                sSessionGuid = userSessionId,
                sCommandJSON = CommandJson(
                    sApplication = "FAM", sService = "GET_NOTE_MASTER", data = Data(
                        alunnoId = studentId.toString()
                    )
                )
            )
            repository.getAllNotes(request).map { noteResource ->
                val selectedTimeFractionId =
                    dataStoreRepository.getInt("app_selected_time_fraction")
                when (noteResource) {
                    is Resource.Success -> {

                        Resource.Success(
                            noteResource.data?.filter {
                                it.studentId == sessionCache.getStudentId() &&
                                        it.idTimeFraction == selectedTimeFractionId
                            } ?: emptyList()
                        )
                    }

                    is Resource.Loading -> {
                        Resource.Loading(
                            noteResource.data?.filter {
                                it.studentId == sessionCache.getStudentId() &&
                                        it.idTimeFraction == selectedTimeFractionId
                            }
                        )
                    }

                    is Resource.Error -> {
                        noteResource
                    }
                }
            }
        } else {
            flow { }
        }
    }
}