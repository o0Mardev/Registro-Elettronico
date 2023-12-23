package com.mardev.registroelettronico.feature_main.domain.use_case

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_main.data.remote.AxiosApi
import com.mardev.registroelettronico.feature_main.data.remote.CommandJson
import com.mardev.registroelettronico.feature_main.data.remote.CommandJsonSerializer
import com.mardev.registroelettronico.feature_main.data.remote.Data
import com.mardev.registroelettronico.feature_main.data.remote.DataSerializer
import com.mardev.registroelettronico.feature_main.data.remote.JsonRequest
import com.mardev.registroelettronico.feature_main.data.remote.JsonRequestSerializer
import javax.inject.Inject

class SetCommunicationRead @Inject constructor(
    private val axiosApi: AxiosApi,
    private val sessionCache: SessionCache
) {
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(JsonRequest::class.java,JsonRequestSerializer())
        .registerTypeAdapter(CommandJson::class.java, CommandJsonSerializer())
        .registerTypeAdapter(Data::class.java, DataSerializer())
        .create()

    suspend operator fun invoke(communicationId: Int, studentId: Int?) {
        val taxCode = sessionCache.getTaxCode()
        val userSessionId = sessionCache.getActiveSession()?.userSessionId


        if (taxCode != null && userSessionId != null) {
            val request = JsonRequest(
                sCodiceFiscale = taxCode,
                sSessionGuid = userSessionId,
                sCommandJSON = CommandJson(
                    sApplication = "FAM",
                    sService = "APP_PROCESS_QUEUE",
                    sModule = "COMUNICAZIONI_READ",
                    data = Data(
                        comunicazioneId = communicationId.toString(),
                        alunnoId = studentId.toString()
                    ),
                ),
            )

            axiosApi.setCommunicationRead(gson.toJson(request))
        }

    }
}