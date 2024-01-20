package com.mardev.registroelettronico.feature_main.domain.use_case

import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.model.DailyEvents
import com.mardev.registroelettronico.feature_main.domain.repository.RetrieveDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class GetEventsByDate @Inject constructor(
    private val repository: RetrieveDataRepository,
) {
    suspend operator fun invoke(date: LocalDate): Flow<Resource<DailyEvents>> = flow {
        val homeworkFlow = repository.getHomeworkByDate(date)
        val lessonsFlow = repository.getLessonsByDate(date)
        val gradesFlow = repository.getGradesByDate(date)
        val communicationFlow = repository.getCommunicationByDate(date)

        combine(
            homeworkFlow,
            lessonsFlow,
            gradesFlow,
            communicationFlow
        ) { homework, lessons, grades, communications ->
            // Combine the individual results into a DailyEvents object
            DailyEvents(
                homework.data ?: emptyList(),
                lessons.data ?: emptyList(),
                grades.data ?: emptyList(),
                communications.data ?: emptyList()
            )
        }.collect { combinedResult ->
            // Emit the combined result as a success resource
            emit(Resource.Success(combinedResult))
        }
    }
}
