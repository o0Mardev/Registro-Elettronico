package com.mardev.registroelettronico.core.data.remote

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.mardev.registroelettronico.core.util.Constants
import java.lang.reflect.Type

data class JsonRequest(
    val sCodiceFiscale: String? = null,
    val sSearch: String? = null,
    val sUserName: String? = null,
    val sPassword: String? = null,
    val sAppName: String? = null,
    val sSessionGuid: String? = null,
    val sCommandJSON: CommandJson? = null,
    val sVendorToken: String = Constants.vendorToken
)

class JsonRequestSerializer : JsonSerializer<JsonRequest> {
    override fun serialize(
        src: JsonRequest?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonObject().apply {
            src?.let { jsonRequest ->
                jsonRequest.sSearch?.let {  addProperty("sSearch", it) }
                jsonRequest.sCodiceFiscale?.let { addProperty("sCodiceFiscale", it) }
                jsonRequest.sUserName?.let { addProperty("sUserName", it) }
                jsonRequest.sPassword?.let { addProperty("sPassword", it) }
                jsonRequest.sAppName?.let { addProperty("sAppName", it) }
                jsonRequest.sSessionGuid?.let { addProperty("sSessionGuid", it) }
                jsonRequest.sCommandJSON?.let { add("sCommandJSON", context?.serialize(it)) }
                addProperty("sVendorToken", jsonRequest.sVendorToken)
            }
        }
    }
}

data class CommandJson(
    val sApplication: String,
    val sService: String,
    val sModule: String? = null,
    val data: Data? = null
)

class CommandJsonSerializer: JsonSerializer<CommandJson>{
    override fun serialize(
        src: CommandJson?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("sApplication", src?.sApplication)
        jsonObject.addProperty("sService", src?.sService)
        jsonObject.addProperty("sModule", src?.sModule)
        jsonObject.add("data", context?.serialize(src?.data))
        return jsonObject
    }

}

data class Data(
    val comunicazioneId: String? = null,
    val dataGiorno: String? = null,
    val alunnoId: String? = null
)


class DataSerializer: JsonSerializer<Data> {
    override fun serialize(
        src: Data?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val jsonObject = JsonObject()
        jsonObject.addProperty("comunicazioneId", src?.comunicazioneId)
        jsonObject.addProperty("dataGiorno", src?.dataGiorno)
        jsonObject.addProperty("alunnoId", src?.alunnoId)
        return jsonObject
    }
}