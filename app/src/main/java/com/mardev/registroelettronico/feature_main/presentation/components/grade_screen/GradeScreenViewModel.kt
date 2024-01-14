package com.mardev.registroelettronico.feature_main.presentation.components.grade_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.use_case.GetGrades
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradeScreenViewModel @Inject constructor(
    private val getGrades: GetGrades
) : ViewModel() {

    private val _state = MutableStateFlow(GradeScreenState())
    val state: StateFlow<GradeScreenState> = _state.asStateFlow()

     init {
        viewModelScope.launch {
            getGrades().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { gradeScreenState ->
                            gradeScreenState.copy(
                                grades = result.data ?: emptyList(),
                                loading = true
                            )
                        }
                        Log.d("TAG", "Loading grades data")
                    }

                    is Resource.Success -> {
                        Log.d("TAG", "Got grades data")
                        _state.update { gradeScreenState ->
                            gradeScreenState.copy(
                                grades = result.data ?: emptyList(),
                                loading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "Error while getting grades data")
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}
