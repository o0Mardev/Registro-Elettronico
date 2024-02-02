package com.mardev.registroelettronico.feature_main.domain.repository

import com.mardev.registroelettronico.core.data.remote.JsonRequest
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.model.Communication
import com.mardev.registroelettronico.feature_main.domain.model.Grade
import com.mardev.registroelettronico.feature_main.domain.model.Homework
import com.mardev.registroelettronico.feature_main.domain.model.Lesson
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface RetrieveDataRepository {

    fun getAllHomework(
        request: JsonRequest
    ): Flow<Resource<List<Homework>>>

    fun getAllLessons(
        request: JsonRequest
    ): Flow<Resource<List<Lesson>>>

    fun getAllGrades(
        request: JsonRequest
    ): Flow<Resource<List<Grade>>>

    fun getAllCommunications(
        request: JsonRequest
    ): Flow<Resource<Pair<Int?, List<Communication>>>>

    suspend fun updateHomeworkState(
        id: Int,
        state: Boolean
    )

    suspend fun getHomeworkByDate(
        date: LocalDate
    ): Flow<Resource<List<Homework>>>


    suspend fun getLessonsByDate(
        date: LocalDate
    ): Flow<Resource<List<Lesson>>>


    suspend fun getGradesByDate(
        date: LocalDate
    ): Flow<Resource<List<Grade>>>


    suspend fun getCommunicationByDate(
        date: LocalDate
    ): Flow<Resource<List<Communication>>>

    suspend fun getHomeworkBySubject(
        subject: String
    ): Flow<Resource<List<Homework>>>
}