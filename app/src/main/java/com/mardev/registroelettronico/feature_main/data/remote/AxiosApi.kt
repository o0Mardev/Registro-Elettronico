package com.mardev.registroelettronico.feature_main.data.remote

import com.mardev.registroelettronico.feature_main.data.remote.dto.communication.CommunicationResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.grades.GradeResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.homework.HomeworkResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.lesson.LessonResponseDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RetrieveDataApi {
    @GET("RetrieveDataInformation")
    suspend fun getHomework(
        @Query("taxCode") taxCode: String,
        @Query("userSession") userSession: String,
        @Query("action") action: String = "GET_COMPITI_MASTER"
    ): HomeworkResponseDto

    @GET("RetrieveDataInformation")
    suspend fun getLessons(
        @Query("taxCode") taxCode: String,
        @Query("userSession") userSession: String,
        @Query("action") action: String = "GET_ARGOMENTI_MASTER"
    ): LessonResponseDto

    @GET("RetrieveDataInformation")
    suspend fun getGrades(
        @Query("taxCode") taxCode: String,
        @Query("userSession") userSession: String,
        @Query("action") action: String = "GET_VOTI_LIST_DETAIL"
    ): GradeResponseDto

    @GET("RetrieveDataInformation")
    suspend fun getCommunications(
        @Query("taxCode") taxCode: String,
        @Query("userSession") userSession: String,
        @Query("action") action: String = "GET_COMUNICAZIONI_MASTER"
    ): CommunicationResponseDto

}