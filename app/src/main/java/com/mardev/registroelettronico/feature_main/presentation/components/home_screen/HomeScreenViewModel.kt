package com.mardev.registroelettronico.feature_main.presentation.components.home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.model.DailyEvents
import com.mardev.registroelettronico.feature_main.domain.use_case.GetCommunications
import com.mardev.registroelettronico.feature_main.domain.use_case.GetEventsByDate
import com.mardev.registroelettronico.feature_main.domain.use_case.GetGrades
import com.mardev.registroelettronico.feature_main.domain.use_case.GetHomework
import com.mardev.registroelettronico.feature_main.domain.use_case.GetLessons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
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
    private val getCommunications: GetCommunications
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    //We could use the combine operator to combine the flows of other screen?
    init {
        viewModelScope.launch {
            async { getHomework().launchIn(viewModelScope) }
            async { getLessons().launchIn(viewModelScope) }
            async { getGrades().launchIn(viewModelScope) }
            async { getCommunications().launchIn(viewModelScope) }
        }
        initializeCalendarState(_state.value.date)
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
        _state.update { homeScreenState ->
            homeScreenState.copy(
                date = c.time
            )
        }
    }

    private fun updateEvents() {
        viewModelScope.launch {
            getEventsByDate(date = _state.value.date).onEach { result ->
                Log.d("TAG", "${result.data}")
                when (result) {
                    is Resource.Loading -> {
                        _state.update { homeScreenState ->
                            homeScreenState.copy(
                                events = result.data ?: DailyEvents()
                            )
                        }
                        Log.d("TAG", "events: ${result.data}")
                    }

                    is Resource.Success -> {
                        _state.update { homeScreenState ->
                            homeScreenState.copy(
                                events = result.data ?: DailyEvents()
                            )
                        }
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

    fun onAddDayButton() {
        updateDateByOffset(1)
    }

    fun onSubtractDayButton() {
        updateDateByOffset(-1)
    }
}
