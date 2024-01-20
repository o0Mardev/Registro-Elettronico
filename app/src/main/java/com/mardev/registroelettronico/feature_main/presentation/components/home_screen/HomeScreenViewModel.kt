package com.mardev.registroelettronico.feature_main.presentation.components.home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.model.DailyEvents
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
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getEventsByDate: GetEventsByDate,
    private val getHomework: GetHomework,
    private val getLessons: GetLessons,
    private val getGrades: GetGrades
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val deferredHomework = async { getHomework() }
            val deferredLessons = async { getLessons() }
            val deferredGrades = async { getGrades() }
            deferredGrades.await().onCompletion {
                updateEvents()
            }.launchIn(viewModelScope)
            deferredHomework.await().onCompletion {
                updateEvents()
            }.launchIn(viewModelScope)
            deferredLessons.await().onCompletion {
                updateEvents()
            }.launchIn(viewModelScope)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("TAG", "onCleared: HomeScreenViewModel")
    }


    private suspend fun updateEvents() {
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

    fun onCurrentDayButtonClick() {
        _state.update { homeScreenState ->
            homeScreenState.copy(date = LocalDate.now())
        }
        viewModelScope.launch {
            updateEvents()
        }
    }

    fun onAddDayButton() {
        _state.update { homeScreenState ->
            homeScreenState.copy(date = homeScreenState.date.plusDays(1))
        }
        viewModelScope.launch {
            updateEvents()
        }
    }

    fun onSubtractDayButton() {
        _state.update { homeScreenState ->
            homeScreenState.copy(date = homeScreenState.date.minusDays(1))
        }
        viewModelScope.launch {
            updateEvents()
        }
    }

    fun onSelectedDay(date: LocalDate){
        _state.update { homeScreenState ->
            homeScreenState.copy(date = date)
        }
        viewModelScope.launch {
            updateEvents()
        }
    }
}
