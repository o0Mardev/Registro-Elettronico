package com.mardev.registroelettronico.feature_main.presentation.components

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_main.domain.model.Student
import com.mardev.registroelettronico.feature_main.domain.use_case.GetStudents
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
class MainViewModel @Inject constructor(
    private val getStudents: GetStudents,
    private val sessionCache: SessionCache
) : ViewModel() {

    private val _showStudentSelector = mutableStateOf(false)
    val showStudentSelector: State<Boolean> = _showStudentSelector

    private val _students = MutableStateFlow(emptyList<Student>())
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    init {
        viewModelScope.launch {
            // If a student has not been selected get the students and make the selector dialog appear
            if (sessionCache.getStudentId()==null){
                getStudents().onEach { result ->
                    Log.d("TAG", "getStudents viewModel")
                    when (result) {
                        is Resource.Success -> {
                            Log.d("TAG", "getStudents viewModel Success")
                            result.data?.let { students ->
                                if (students.size > 1){
                                    _students.update {
                                        students
                                    }
                                    _showStudentSelector.value = true
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
            } else _showStudentSelector.value = false
        }
    }

    fun onSaveStudentId(selectedStudent: Student){
        Log.d("TAG", "onSaveStudentId: saving $selectedStudent")
        viewModelScope.launch {
            sessionCache.saveStudentId(selectedStudent.studentId)
        }
        _showStudentSelector.value = false
    }
}