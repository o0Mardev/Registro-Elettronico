package com.mardev.registroelettronico.feature_main.presentation.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_main.domain.model.Student
import com.mardev.registroelettronico.feature_main.domain.use_case.GetStudents
import com.mardev.registroelettronico.feature_main.domain.use_case.GetTimeFractions
import com.mardev.registroelettronico.feature_main.presentation.MainScreenState
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
class MainViewModel @Inject constructor(
    private val getStudents: GetStudents,
    private val sessionCache: SessionCache,
    private val getTimeFractions: GetTimeFractions
) : ViewModel() {


    private val _state = MutableStateFlow(MainScreenState())
    val state: StateFlow<MainScreenState> = _state.asStateFlow()


    init {
        viewModelScope.launch {

            getTimeFractions().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { timeFractions ->
                            _state.update { mainScreenState ->
                                mainScreenState.copy(timeFractions = timeFractions)
                            }
                        }
                    }

                    is Resource.Loading -> {
                        result.data?.let { timeFractions ->
                            _state.update { mainScreenState ->
                                mainScreenState.copy(timeFractions = timeFractions)
                            }
                        }
                    }

                    is Resource.Error -> {
                        Timber.e("Error while getting time fractions")
                    }

                }
            }.launchIn(viewModelScope)


            // If a student has not been selected get the students and make the selector dialog appear
            if (sessionCache.getStudentId()==null){
                getStudents().onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { students ->
                                if (students.size > 1){
                                    _state.update { mainScreenState ->
                                        mainScreenState.copy(
                                            students = students,
                                            showStudentSelector = true
                                        )
                                    }
                                } else {
                                    sessionCache.saveStudentId(students.first().studentId)
                                }
                            }
                        }

                        is Resource.Loading -> {

                        }

                        is Resource.Error -> {

                        }
                    }
                }.launchIn(viewModelScope)
            } else _state.update { mainScreenState ->
                mainScreenState.copy(showStudentSelector = false)
            }
        }
    }

    fun onSaveStudentId(selectedStudent: Student){
        viewModelScope.launch {
            sessionCache.saveStudentId(selectedStudent.studentId)
        }
        _state.update { mainScreenState ->
            mainScreenState.copy(showStudentSelector = false)
        }
    }


    fun showThreeDotsMenu(value: Boolean){
        _state.update { mainScreenState ->
            mainScreenState.copy(showThreeDotsMenu = value)
        }
    }
}