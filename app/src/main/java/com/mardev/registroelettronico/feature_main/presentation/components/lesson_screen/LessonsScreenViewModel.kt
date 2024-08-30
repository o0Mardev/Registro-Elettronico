package com.mardev.registroelettronico.feature_main.presentation.components.lesson_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.use_case.GetLessons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LessonsScreenViewModel @Inject constructor(
    private val getLessons: GetLessons
) : ViewModel() {

    private val _state = MutableStateFlow(LessonScreenState())
    val state: StateFlow<LessonScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getLessons().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { lessonScreenState ->
                            lessonScreenState.copy(
                                lessons = result.data ?: emptyList(),
                                loading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update { lessonScreenState ->
                            lessonScreenState.copy(
                                lessons = result.data ?: emptyList(),
                                loading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        Timber.e("Error while getting lessons")
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}
