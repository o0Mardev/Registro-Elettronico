package com.mardev.registroelettronico.core.data.remote

import android.util.Base64
import com.google.gson.JsonObject
import com.mardev.registroelettronico.core.util.Constants.rc4Key
import com.mardev.registroelettronico.core.util.RC4
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.IOException
import java.net.URLEncoder


class Interceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val encryptionService = RC4(rc4Key.toByteArray())

        val originalRequest = chain.request()
        val url = originalRequest.url()

        val jsonRequest = url.queryParameter("jsonRequest")
//        Timber.d("intercept: jsonRequest $jsonRequest")

        if (jsonRequest != null) {
            when (originalRequest.method()) {
                "GET" -> {
                    val encryptedJsonRequest = encryptionService.encrypt(jsonRequest.toByteArray())
                    val base64EncryptedJsonRequest =
                        Base64.encode(encryptedJsonRequest, Base64.NO_WRAP).decodeToString()

                    val modifiedUrl = url.newBuilder()
                        .removeAllQueryParameters("jsonRequest")
                        .addQueryParameter(
                            "base64EncryptedJsonRequest",
                            if (url.pathSegments().last() == "Login2") URLEncoder.encode(
                                base64EncryptedJsonRequest,
                                "utf-8"
                            ) else base64EncryptedJsonRequest
                        )
                        .build()


                    val modifiedRequest = originalRequest.newBuilder()
                        .url(modifiedUrl)
                        .build()

//                    Timber.d(modifiedRequest.url().toString())

                    val response = chain.proceed(modifiedRequest)

                    return decryptResponse(response, encryptionService)
                }

                "POST" -> {
                    val encryptedJsonRequest = encryptionService.encrypt(jsonRequest.toByteArray())
                    val base64EncryptedJsonRequest =
                        Base64.encode(encryptedJsonRequest, Base64.NO_WRAP).decodeToString()

                    val modifiedUrl = url.newBuilder()
                        .removeAllQueryParameters("jsonRequest")
                        .build()

                    val jsonObject = JsonObject()
                    jsonObject.addProperty("JsonRequest", base64EncryptedJsonRequest)


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

        try {
            val modifiedResponseBody = responseBody?.let {
                val responseString = it.string()
//                Timber.d("responseString: $responseString")
                val base64Response = Base64.decode(responseString, Base64.NO_WRAP).toString(Charsets.ISO_8859_1)
                val decryptedResponse = encryptionService.decrypt(base64Response.toByteArray(Charsets.ISO_8859_1))
                val decodedDecryptedResponse = decryptedResponse.toString(Charsets.ISO_8859_1)
//                Timber.d("decryptedResponse: $decodedDecryptedResponse")

                ResponseBody.create(
                    it.contentType(),
                    decodedDecryptedResponse
                )
            }

            return response.newBuilder()
                .body(modifiedResponseBody)
                .build()

        } catch (e: IllegalArgumentException){
            // note this happens for example when the axios servers are in maintenance
            // and they throw an unencrypted html page which can't be decoded obviously
            Timber.e("I was caught: $e")
            throw IOException()
        }

    }

}
