package com.mardev.registroelettronico.feature_home.presentation.components.homework_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_home.domain.repository.RetrieveDataRepository
import com.mardev.registroelettronico.feature_home.domain.use_case.GetHomework
import com.mardev.registroelettronico.feature_home.domain.use_case.GetLessons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeworkScreenViewModel @Inject constructor(
    private val getHomework: GetHomework,
    private val repository: RetrieveDataRepository
) : ViewModel() {

    private val _state = mutableStateOf(HomeworkScreenState())
    val state: State<HomeworkScreenState> = _state

    init {
        viewModelScope.launch {
            getHomework().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            homework = result.data ?: emptyList(),
                            loading = true
                        )
                        Log.d("TAG", "Loading homework data")
                    }

                    is Resource.Success -> {
                        Log.d("TAG", "Got homework data")
                        _state.value = _state.value.copy(
                            homework = result.data ?: emptyList(),
                            loading = false
                        )
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "Error while getting homework data")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun checkHomework(id: Int, state: Boolean) {
        Log.d("TAG", "checkHomework: id $id and state $state")
        viewModelScope.launch {
            repository.updateHomeworkState(id, state)
        }
        _state.value = _state.value.copy(
            homework = _state.value.homework.map { homework ->
                if (homework.id == id) {
                    // Update the state for the specific item
                    homework.copy(completed = state)
                } else {
                    homework
                }
            })
    }
}
