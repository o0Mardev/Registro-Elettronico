package com.mardev.registroelettronico.feature_main.common.domain.repository

import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.common.domain.model.Communication
import com.mardev.registroelettronico.feature_main.common.domain.model.Grade
import com.mardev.registroelettronico.feature_main.common.domain.model.Homework
import com.mardev.registroelettronico.feature_main.common.domain.model.Lesson
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface RetrieveDataRepository {

    fun getAllHomework(
        taxCode: String,
        userSession: String
    ): Flow<Resource<List<Homework>>>

    fun getAllLessons(
        taxCode: String,
        userSession: String
    ): Flow<Resource<List<Lesson>>>

    fun getAllGrades(
        taxCode: String,
        userSession: String,
    ): Flow<Resource<List<Grade>>>

    fun getAllCommunications(
        taxCode: String,
        userSession: String
    ): Flow<Resource<List<Communication>>>

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