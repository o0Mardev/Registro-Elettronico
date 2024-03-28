package com.mardev.registroelettronico.feature_main.domain.use_case

import android.util.Log
import com.mardev.registroelettronico.core.data.remote.CommandJson
import com.mardev.registroelettronico.core.data.remote.Data
import com.mardev.registroelettronico.core.data.remote.JsonRequest
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_main.domain.model.GenericAbsence
import com.mardev.registroelettronico.feature_main.domain.repository.RetrieveDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAbsences(
    private val repository: RetrieveDataRepository,
    private val sessionCache: SessionCache
) {
    suspend operator fun invoke(): Flow<Resource<List<GenericAbsence>>> {
        val taxCode = sessionCache.getTaxCode()
        val userSessionId = sessionCache.getActiveSession()?.userSessionId
        val studentId = sessionCache.getStudentId()
        Log.d("TAG", "invoke: taxCode $taxCode")
        Log.d("TAG", "invoke: userSessionId $userSessionId")
        Log.d("TAG", "invoke: studentId $studentId")

        return if (taxCode != null && userSessionId != null && studentId != null) {
            Log.d("TAG", "invoke: taxCode, userSessionId and studentId not null")
            val request = JsonRequest(
                sCodiceFiscale = taxCode,
                sSessionGuid = userSessionId,
                sCommandJSON = CommandJson(
                    sApplication = "FAM", sService = "GET_ASSENZE_MASTER", data = Data(
                        alunnoId = studentId.toString()
                    )
                )
            )
            repository.getAllAbsences(request, studentId)
        } else {
            flow { }
        }
    }
}
