package com.mardev.registroelettronico.feature_main.presentation.components.grade_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.use_case.GetGrades
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GradeScreenViewModel @Inject constructor(
    private val getGrades: GetGrades
) : ViewModel() {

    private val _state = mutableStateOf(GradeScreenState())
    val state: State<GradeScreenState> = _state

     init {
        viewModelScope.launch {
            getGrades().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            grades = result.data ?: emptyList(),
                            loading = true
                        )
                        Log.d("TAG", "Loading grades data")
                    }

                    is Resource.Success -> {
                        Log.d("TAG", "Got grades data")
                        _state.value = _state.value.copy(
                            grades = result.data ?: emptyList(),
                            loading = false
                        )
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "Error while getting grades data")
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}
