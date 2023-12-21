package com.mardev.registroelettronico.feature_authentication.data.remote

import com.mardev.registroelettronico.feature_authentication.data.remote.dto.LoginInfoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthenticationApi {

    @GET("Login")
    suspend fun login(
        @Query("taxCode") taxCode: String,
        @Query("username") username: String,
        @Query("password") password: String
    ): LoginInfoDto
}