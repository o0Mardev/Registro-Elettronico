package com.mardev.registroelettronico.feature_main.presentation.components.grade_screen

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
import timber.log.Timber
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
                        result.data?.let { grades ->
                            _state.update { gradeScreenState ->
                                gradeScreenState.copy(
                                    allGrades = grades,
                                    filteredGrades = grades,
                                    loading = true
                                )
                            }
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { grades ->
                            _state.update { gradeScreenState ->
                                gradeScreenState.copy(
                                    allGrades = grades,
                                    filteredGrades = grades,
                                    loading = false
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        Timber.e("Error while getting grades data")
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun updateGradesForSelectedTimeFraction(selectedTimeFraction: Int? = null) {
        _state.update { gradeScreenState ->
            gradeScreenState.copy(
                filteredGrades = if (selectedTimeFraction != null) gradeScreenState.allGrades.filter { it.idTimeFraction == selectedTimeFraction } else {
                    gradeScreenState.allGrades
                }
            )
        }

    }
}
