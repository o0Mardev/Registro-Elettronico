package com.mardev.registroelettronico.core.data.remote

import com.mardev.registroelettronico.feature_authentication.data.remote.dto.login.LoginInfoResponseDto
import com.mardev.registroelettronico.feature_authentication.data.remote.dto.search.SchoolResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.absences.AbsenceResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.communication.CommunicationReadResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.communication.CommunicationResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.grades.GradeResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.homework.HomeworkResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.lesson.LessonResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.notes.NoteResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.structural.StructuralResponseDto
import com.mardev.registroelettronico.feature_main.data.remote.dto.students.StudentsResponseDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AxiosApi {
    @GET("Login2")
    suspend fun login(
        @Query("jsonRequest") jsonRequest: String
    ): LoginInfoResponseDto

    @GET("RetrieveAPPCustomerInformationByString")
    suspend fun searchForSchools(
        @Query("jsonRequest") jsonRequest: String
    ): SchoolResponseDto

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
    @GET("RetrieveDataInformation")
    suspend fun getAbsences(
        @Query("jsonRequest") jsonRequest: String
    ): AbsenceResponseDto

    @GET("RetrieveDataInformation")
    suspend fun getNotes(
        @Query("jsonRequest") jsonRequest: String
    ): NoteResponseDto

    @POST("ExecuteCommand")
    suspend fun setCommunicationRead(
        @Query("jsonRequest") jsonRequest: String
    ): CommunicationReadResponseDto

    @GET("RetrieveDataInformation")
    suspend fun getStudents(
        @Query("jsonRequest") jsonRequest: String
    ): StudentsResponseDto

    @GET("RetrieveDataInformation")
    suspend fun getStructural(
        @Query("jsonRequest") jsonRequest: String
    ): StructuralResponseDto
}