package com.mardev.registroelettronico.feature_main.domain.repository

import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.data.remote.JsonRequest
import com.mardev.registroelettronico.feature_main.domain.model.Communication
import com.mardev.registroelettronico.feature_main.domain.model.Grade
import com.mardev.registroelettronico.feature_main.domain.model.Homework
import com.mardev.registroelettronico.feature_main.domain.model.Lesson
import kotlinx.coroutines.flow.Flow
import java.util.Date

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
        date: Date
    ): Flow<Resource<List<Homework>>>


    suspend fun getLessonsByDate(
        date: Date
    ): Flow<Resource<List<Lesson>>>


    suspend fun getGradesByDate(
        date: Date
    ): Flow<Resource<List<Grade>>>


    suspend fun getCommunicationByDate(
        date: Date
    ): Flow<Resource<List<Communication>>>

}