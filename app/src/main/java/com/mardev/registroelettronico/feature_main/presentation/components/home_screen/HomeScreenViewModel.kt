package com.mardev.registroelettronico.feature_main.presentation.components.home_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.model.DailyEvents
import com.mardev.registroelettronico.feature_main.domain.repository.RetrieveDataRepository
import com.mardev.registroelettronico.feature_main.domain.use_case.GetCommunications
import com.mardev.registroelettronico.feature_main.domain.use_case.GetEventsByDate
import com.mardev.registroelettronico.feature_main.domain.use_case.GetGrades
import com.mardev.registroelettronico.feature_main.domain.use_case.GetHomework
import com.mardev.registroelettronico.feature_main.domain.use_case.GetLessons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getEventsByDate: GetEventsByDate,
    private val getHomework: GetHomework,
    private val getLessons: GetLessons,
    private val getGrades: GetGrades,
    private val getCommunications: GetCommunications,
    private val repository: RetrieveDataRepository
) : ViewModel() {

    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state

    init {
        viewModelScope.launch {
            async { getHomework().launchIn(viewModelScope) }
            async { getLessons().launchIn(viewModelScope) }
            async { getGrades().launchIn(viewModelScope) }
            async { getCommunications().launchIn(viewModelScope) }
        }
        initializeCalendarState(state.value.date)
        updateEvents()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("TAG", "onCleared: HomeScreenViewModel")
    }

    private fun initializeCalendarState(date: Date) {
        val c = Calendar.getInstance()
        c.time = date
        c.set(Calendar.HOUR_OF_DAY, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.MILLISECOND, 0)
        _state.value = _state.value.copy(
            date = c.time
        )
    }

    private fun updateEvents() {
        viewModelScope.launch {
            getEventsByDate(date = _state.value.date).onEach { result ->
                Log.d("TAG", "${result.data}")
                when (result) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            events = result.data ?: DailyEvents()
                        )
                        Log.d("TAG", "events: ${result.data}")
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            events = result.data ?: DailyEvents()
                        )
                        Log.d("TAG", "events: ${result.data}")
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "events: ${result.data}")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun updateDateByOffset(offset: Int) {
        val c = Calendar.getInstance()
        c.time = state.value.date
        c.add(Calendar.DATE, offset)
        initializeCalendarState(c.time)
        Log.d("TAG", "Updated date: ${state.value.date}")
        updateEvents()
    }

    fun onCurrentDayButtonClick() {
        initializeCalendarState(Date())
        updateEvents()
    }

    fun checkHomework(id: Int, state: Boolean) {
        Log.d("TAG", "checkHomework: id $id and state $state")
        viewModelScope.launch {
            repository.updateHomeworkState(id, state)
        }
        _state.value = _state.value.copy(
            events = _state.value.events.copy(_state.value.events.homework.map { homework ->
                if (homework.id == id) {
                    // Update the state for the specific item
                    homework.copy(completed = state)
                } else {
                    homework
                }
            })
        )
    }

    fun onAddDayButton() {
        updateDateByOffset(1)
    }

    fun onSubtractDayButton() {
        updateDateByOffset(-1)
    }
}
