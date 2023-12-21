package com.mardev.registroelettronico.feature_main.data.remote

import com.mardev.registroelettronico.feature_main.data.remote.dto.communication.CommunicationResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.grades.GradeResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.homework.HomeworkResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.lesson.LessonResponseDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AxiosApi {
    @GET("RetrieveDataInformation")
    suspend fun getHomework(
        @Query("jsonRequest") jsonRequest: String
    ): HomeworkResponseDto

    @GET("RetrieveDataInformation")
    suspend fun getLessons(
        @Query("jsonRequest") jsonRequest: String
    ): LessonResponseDto

    @GET("RetrieveDataInformation")
    suspend fun getGrades(
        @Query("jsonRequest") jsonRequest: String
    ): GradeResponseDto

    @GET("RetrieveDataInformation")
    suspend fun getCommunications(
        @Query("jsonRequest") jsonRequest: String
    ): CommunicationResponseDto


    @POST("ExecuteCommand")
    suspend fun setCommunicationRead(
        @Query("jsonRequest") jsonRequest: String
    )

}