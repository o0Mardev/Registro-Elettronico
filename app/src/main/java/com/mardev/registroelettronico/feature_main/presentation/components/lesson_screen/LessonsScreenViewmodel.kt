package com.mardev.registroelettronico.feature_main.presentation.components.lesson_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.use_case.GetLessons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonsScreenViewmodel @Inject constructor(
    private val getLessons: GetLessons
) : ViewModel() {

    private val _state = mutableStateOf(LessonScreenState())
    val state: State<LessonScreenState> = _state

    init {
        viewModelScope.launch {
            getLessons().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            lessons = result.data ?: emptyList(),
                            loading = true
                        )
                        Log.d("TAG", "Loading lessons data")
                    }

                    is Resource.Success -> {
                        Log.d("TAG", "Got lessons data")
                        _state.value = _state.value.copy(
                            lessons = result.data ?: emptyList(),
                            loading = false
                        )
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "Error while getting lessons data")
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}
