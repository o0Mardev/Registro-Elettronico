package com.mardev.registroelettronico.feature_main.domain.use_case

import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_main.domain.model.DailyEvents
import com.mardev.registroelettronico.feature_main.domain.repository.RetrieveDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class GetEventsByDate @Inject constructor(
    private val repository: RetrieveDataRepository,
    private val sessionCache: SessionCache
) {
    suspend operator fun invoke(date: LocalDate): Flow<Resource<DailyEvents>> = flow {
        val homeworkFlow = repository.getHomeworkByDate(date, sessionCache.getStudentId())
        val lessonsFlow = repository.getLessonsByDate(date, sessionCache.getStudentId())
        val gradesFlow = repository.getGradesByDate(date, sessionCache.getStudentId())
        val absencesFlow = repository.getAbsencesByDate(date, sessionCache.getStudentId())
        val notesFlow = repository.getNotesByDate(date, sessionCache.getStudentId())

        combine(
            homeworkFlow,
            lessonsFlow,
            gradesFlow,
            absencesFlow,
            notesFlow
        ) { homework, lessons, grades, absences, notes ->
            // Combine the individual results into a DailyEvents object
            DailyEvents(
                homework =homework.data ?: emptyList(),
                lessons = lessons.data ?: emptyList(),
                grades = grades.data ?: emptyList(),
                absences = absences.data ?: emptyList(),
                notes = notes.data ?: emptyList()
            )
        }.collect { combinedResult ->
            // Emit the combined result as a success resource
            emit(Resource.Success(combinedResult))
        }
    }
}
