package com.mardev.registroelettronico.feature_authentication.data.remote

import android.util.Base64
import android.util.Log
import com.google.gson.JsonObject
import com.mardev.registroelettronico.core.util.Constants
import com.mardev.registroelettronico.core.util.RC4
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

class Interceptor(
    private val encryptionService: RC4
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url()

        val taxCodeParam = url.queryParameter("taxCode")
        val usernameParam = url.queryParameter("username")
        val passwordParam = url.queryParameter("password")

        if (taxCodeParam!=null && usernameParam!=null && passwordParam!=null) {
            val nonEncryptedJson =
                createJsonString(taxCodeParam, usernameParam, passwordParam)
            val encryptedJson = encryptionService.encrypt(nonEncryptedJson.toByteArray())
            val base64EncryptedJSon = Base64.encode(encryptedJson, Base64.NO_WRAP).decodeToString()

            val modifiedUrl = url.newBuilder()
                .removeAllQueryParameters("taxCode")
                .removeAllQueryParameters("username")
                .removeAllQueryParameters("password")
                .addQueryParameter(
                    "base64EncryptedJson",
                    base64EncryptedJSon
                )
                .build()

            val modifiedRequest = originalRequest.newBuilder()
                .url(modifiedUrl)
                .build()

            Log.d("TAG", "intercept: modifiedUrl=$modifiedUrl")
            Log.d("TAG", "intercept: modifiedRequest=$modifiedRequest")

            val response = chain.proceed(modifiedRequest)
            return decryptResponse(response)
        }
        return chain.proceed(originalRequest)
    }

    private fun decryptResponse(response: Response): Response {
        val responseBody = response.body()

        val modifiedResponseBody = responseBody?.let {
            val responseString = it.string()
            val base64Response = Base64.decode(responseString, Base64.NO_WRAP).toString(Charsets.ISO_8859_1)
            val decryptedResponse = encryptionService.decrypt(base64Response.toByteArray(Charsets.ISO_8859_1))
            val decodedDecryptedResponse = decryptedResponse.toString(Charsets.ISO_8859_1)
            Log.d("TAG", "login: $decodedDecryptedResponse")
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
        username: String,
        password: String
    ): String {
        val jsonObject = JsonObject()
        jsonObject.addProperty("sCodiceFiscale", taxCode)
        jsonObject.addProperty("sUserName", username)
        jsonObject.addProperty("sPassword", password)
        jsonObject.addProperty("sAppName", "")
        jsonObject.addProperty("sVendorToken", Constants.vendorToken)
        return jsonObject.toString()
    }

}
