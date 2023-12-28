package com.mardev.registroelettronico.feature_main.data.remote

import android.util.Base64
import android.util.Log
import com.google.gson.JsonObject
import com.mardev.registroelettronico.core.util.Constants.rc4Key
import com.mardev.registroelettronico.core.util.RC4
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist


class Interceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val encryptionService = RC4(rc4Key.toByteArray())

        val originalRequest = chain.request()
        val url = originalRequest.url()

        val jsonRequest = url.queryParameter("jsonRequest")
        Log.d("TAG", "intercept: jsonRequest $jsonRequest")

        if (jsonRequest != null) {
            when (originalRequest.method()) {
                "GET" -> {
                    Log.d("TAG", "intercept: nonEncryptedJson $jsonRequest")
                    val encryptedJsonRequest = encryptionService.encrypt(jsonRequest.toByteArray())
                    val base64EncryptedJsonRequest =
                        Base64.encode(encryptedJsonRequest, Base64.NO_WRAP).decodeToString()


                    val modifiedUrl = url.newBuilder()
                        .removeAllQueryParameters("jsonRequest")
                        .addQueryParameter("base64EncryptedJsonRequest", base64EncryptedJsonRequest)
                        .build()

                    val modifiedRequest = originalRequest.newBuilder()
                        .url(modifiedUrl)
                        .build()

                    val response = chain.proceed(modifiedRequest)

                    return decryptResponse(response, encryptionService)
                }

                "POST" -> {
                    Log.d("TAG", "intercept: nonEncryptedJson $jsonRequest")
                    val encryptedJsonRequest = encryptionService.encrypt(jsonRequest.toByteArray())
                    val base64EncryptedJsonRequest =
                        Base64.encode(encryptedJsonRequest, Base64.NO_WRAP).decodeToString()

                    val modifiedUrl = url.newBuilder()
                        .removeAllQueryParameters("jsonRequest")
                        .build()

                    val jsonObject = JsonObject()
                    jsonObject.addProperty("JsonRequest", base64EncryptedJsonRequest)

                    Log.d("TAG", "intercept: jsonObject $jsonObject")

                    val jsonObjectAsString = jsonObject.toString()

                    val formBody: RequestBody = RequestBody.create(
                        okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        jsonObjectAsString
                    )

                    val modifiedRequest = originalRequest.newBuilder()
                        .url(modifiedUrl)
                        .post(formBody)
                        .build()
                    val response = chain.proceed(modifiedRequest)
                    return decryptResponse(response, encryptionService)
                }
            }

        }
        throw Exception("Unexpected jsonRequest")
    }


    private fun decryptResponse(response: Response, encryptionService: RC4): Response {
        val responseBody = response.body()

        val modifiedResponseBody = responseBody?.let {
            val responseString = it.string()
            val base64Response = Base64.decode(responseString, Base64.NO_WRAP).toString(Charsets.ISO_8859_1)
            val decryptedResponse = encryptionService.decrypt(base64Response.toByteArray(Charsets.ISO_8859_1))
            val decodedDecryptedResponse = decryptedResponse.toString(Charsets.ISO_8859_1)

            val unescapedString = decodedDecryptedResponse.replace("\\u003c", "<").replace("\\u003e", ">")
            val cleanedResponse = Jsoup.clean(unescapedString, Safelist.none())

            Log.d("TAG", "unCleanedResponse $unescapedString")
            Log.d("TAG", "cleanedResponse: $cleanedResponse")

            ResponseBody.create(
                it.contentType(),
                cleanedResponse
            )
        }

        return response.newBuilder()
            .body(modifiedResponseBody)
            .build()
    }

}
