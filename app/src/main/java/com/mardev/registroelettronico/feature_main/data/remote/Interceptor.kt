package com.mardev.registroelettronico.feature_main.common.data.remote

import android.util.Base64
import com.google.gson.JsonObject
import com.mardev.registroelettronico.core.util.Constants
import com.mardev.registroelettronico.core.util.Constants.rc4Key
import com.mardev.registroelettronico.core.util.RC4
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

class Interceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val encryptionService = RC4(rc4Key.toByteArray())

        val originalRequest = chain.request()
        val url = originalRequest.url()

        val taxCodeParam = url.queryParameter("taxCode")
        val userSessionParam = url.queryParameter("userSession")
        val actionParam = url.queryParameter("action")

        if (taxCodeParam!=null && userSessionParam!=null && actionParam!=null) {
            val nonEncryptedJson =
                createJsonString(taxCodeParam, userSessionParam, actionParam)
            val encryptedJson = encryptionService.encrypt(nonEncryptedJson.toByteArray())
            val base64EncryptedJSon = Base64.encode(encryptedJson, Base64.NO_WRAP).decodeToString()

            val modifiedUrl = url.newBuilder()
                .removeAllQueryParameters("taxCode")
                .removeAllQueryParameters("userSession")
                .removeAllQueryParameters("action")
                .addQueryParameter(
                    "base64EncryptedJson",
                    base64EncryptedJSon
                )
                .build()

            val modifiedRequest = originalRequest.newBuilder()
                .url(modifiedUrl)
                .build()

            val response = chain.proceed(modifiedRequest)

            return decryptResponse(response, encryptionService)
        }
        return chain.proceed(originalRequest)
    }



    private fun decryptResponse(response: Response, encryptionService: RC4): Response {
        val responseBody = response.body()

        val modifiedResponseBody = responseBody?.let {
            val responseString = it.string()
            val base64Response = Base64.decode(responseString, Base64.NO_WRAP).toString(Charsets.ISO_8859_1)
            val decryptedResponse = encryptionService.decrypt(base64Response.toByteArray(Charsets.ISO_8859_1))
            val decodedDecryptedResponse = decryptedResponse.toString(Charsets.ISO_8859_1)
            //Log.d("TAG", decodedDecryptedResponse)
            ResponseBody.create(
                it.contentType(),
                decodedDecryptedResponse
            )
        }

        return response.newBuilder()
            .body(modifiedResponseBody)
            .build()
    }

    private fun createJsonString(
        taxCode: String,
        userSession: String,
        action: String
    ): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("sCodiceFiscale", taxCode)
        jsonObject.addProperty("sSessionGuid", userSession)

        // Create the nested JSON object for sCommandJSON
        val sCommandJson = JsonObject()
        sCommandJson.addProperty("sApplication", "FAM")
        sCommandJson.addProperty("sService", action)
        jsonObject.add("sCommandJSON", sCommandJson)
        jsonObject.addProperty("sVendorToken", Constants.vendorToken)
        return jsonObject.toString()
    }

}
