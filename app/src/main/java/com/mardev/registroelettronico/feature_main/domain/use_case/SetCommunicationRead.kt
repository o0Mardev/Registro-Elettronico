package com.mardev.registroelettronico.feature_main.domain.use_case

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mardev.registroelettronico.R
import com.mardev.registroelettronico.core.data.remote.AxiosApi
import com.mardev.registroelettronico.core.data.remote.CommandJson
import com.mardev.registroelettronico.core.data.remote.CommandJsonSerializer
import com.mardev.registroelettronico.core.data.remote.Data
import com.mardev.registroelettronico.core.data.remote.DataSerializer
import com.mardev.registroelettronico.core.data.remote.JsonRequest
import com.mardev.registroelettronico.core.data.remote.JsonRequestSerializer
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.core.util.UIText
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SetCommunicationRead @Inject constructor(
    private val axiosApi: AxiosApi,
    private val sessionCache: SessionCache
) {
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(JsonRequest::class.java, JsonRequestSerializer())
        .registerTypeAdapter(CommandJson::class.java, CommandJsonSerializer())
        .registerTypeAdapter(Data::class.java, DataSerializer())
        .create()

    suspend operator fun invoke(communicationId: Int, studentId: Int?): Resource<Unit> {
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
            return try {
                val communicationReadResponseDto = axiosApi.setCommunicationRead(gson.toJson(request))

                if (communicationReadResponseDto.errorcode == 0 && communicationReadResponseDto.response == "OK") {
                    Resource.Success(Unit)
                } else Resource.Error(uiText = UIText.DynamicString(communicationReadResponseDto.errormessage))

            } catch (e: HttpException) {
                Resource.Error(uiText = UIText.StringResource(R.string.error1))
            } catch (e: IOException) {
                Resource.Error(uiText = UIText.StringResource(R.string.error2))
            }
        }
        return Resource.Error(uiText = UIText.DynamicString("Error"))
    }
}