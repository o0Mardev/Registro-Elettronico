package com.mardev.registroelettronico.feature_main.domain.repository

import com.mardev.registroelettronico.core.data.remote.JsonRequest
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.model.GenericAbsence
import com.mardev.registroelettronico.feature_main.domain.model.Communication
import com.mardev.registroelettronico.feature_main.domain.model.Grade
import com.mardev.registroelettronico.feature_main.domain.model.Homework
import com.mardev.registroelettronico.feature_main.domain.model.Lesson
import com.mardev.registroelettronico.feature_main.domain.model.Note
import com.mardev.registroelettronico.feature_main.domain.model.Student
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface RetrieveDataRepository {

    fun getAllHomework(
        request: JsonRequest,
        studentId: Int?
    ): Flow<Resource<List<Homework>>>

    fun getAllLessons(
        request: JsonRequest,
        studentId: Int?
    ): Flow<Resource<List<Lesson>>>

    fun getAllGrades(
        request: JsonRequest,
        studentId: Int?
    ): Flow<Resource<List<Grade>>>

    fun getAllCommunications(
        request: JsonRequest,
        studentId: Int?
    ): Flow<Resource<List<Communication>>>

    fun getAllNotes(
        request: JsonRequest,
        studentId: Int?
    ): Flow<Resource<List<Note>>>

    fun getAllAbsences(
        request: JsonRequest,
        studentId: Int?
    ): Flow<Resource<List<GenericAbsence>>>

    fun getAllStudents(
        request: JsonRequest
    ): Flow<Resource<List<Student>>>

    suspend fun updateHomeworkState(
        id: Int,
        state: Boolean
    )

    suspend fun getHomeworkByDate(
        date: LocalDate,
        studentId: Int?
    ): Flow<Resource<List<Homework>>>


    suspend fun getLessonsByDate(
        date: LocalDate,
        studentId: Int?
    ): Flow<Resource<List<Lesson>>>


    suspend fun getGradesByDate(
        date: LocalDate,
        studentId: Int?
    ): Flow<Resource<List<Grade>>>


    suspend fun getCommunicationByDate(
        date: LocalDate,
        studentId: Int?
    ): Flow<Resource<List<Communication>>>

    suspend fun getAbsencesByDate(
        date: LocalDate,
        studentId: Int?
    ): Flow<Resource<List<GenericAbsence>>>

    suspend fun getNotesByDate(
        date: LocalDate,
        studentId: Int?
    ): Flow<Resource<List<Note>>>

}