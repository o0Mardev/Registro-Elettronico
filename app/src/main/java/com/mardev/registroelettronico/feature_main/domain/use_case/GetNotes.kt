package com.mardev.registroelettronico.feature_main.domain.use_case

import android.util.Log
import com.mardev.registroelettronico.core.data.remote.CommandJson
import com.mardev.registroelettronico.core.data.remote.Data
import com.mardev.registroelettronico.core.data.remote.JsonRequest
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_main.domain.model.Note
import com.mardev.registroelettronico.feature_main.domain.repository.RetrieveDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNotes(
    private val repository: RetrieveDataRepository,
    private val sessionCache: SessionCache
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
            repository.getAllNotes(request, studentId)
        } else {
            flow { }
        }
    }
}