package com.mardev.registroelettronico.feature_main.presentation.components.absence

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.model.TypeOfAbsence
import com.mardev.registroelettronico.feature_main.domain.use_case.GetAbsences
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
class AbsenceScreenViewModel @Inject constructor(
    private val getAbsences: GetAbsences
) : ViewModel() {
    private val _state = MutableStateFlow(AbsenceScreenState())
    val state: StateFlow<AbsenceScreenState> = _state.asStateFlow()

    init {
        Log.d("TAG", "Init block")
        viewModelScope.launch {
            getAbsences().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d("TAG", "Loading absences data")
                        result.data?.let { genericAbsences ->
                            val groupedGenericAbsences =
                                genericAbsences.groupBy { it.typeOfAbsence }
                            groupedGenericAbsences.forEach { (type, genericAbsences) ->
                                when (type) {
                                    TypeOfAbsence.ABSENCE -> {
                                        _state.update { absenceScreenState ->
                                            absenceScreenState.copy(absences = genericAbsences)
                                        }
                                    }

                                    TypeOfAbsence.DELAY -> {
                                        _state.update { absenceScreenState ->
                                            absenceScreenState.copy(delays = genericAbsences)
                                        }
                                    }
                                    TypeOfAbsence.EXIT -> {
                                        _state.update { absenceScreenState ->
                                            absenceScreenState.copy(exits = genericAbsences)
                                        }
                                    }
                                    TypeOfAbsence.UNKNOWN -> {}
                                }
                            }
                        }

                    }

                    is Resource.Success -> {
                        Log.d("TAG", "Got absences data")
                        result.data?.let { genericAbsences ->
                            val groupedGenericAbsences =
                                genericAbsences.groupBy { it.typeOfAbsence }
                            groupedGenericAbsences.forEach { (type, genericAbsences) ->
                                when (type) {
                                    TypeOfAbsence.ABSENCE -> {
                                        _state.update { absenceScreenState ->
                                            absenceScreenState.copy(absences = genericAbsences, loading = false)
                                        }
                                    }

                                    TypeOfAbsence.DELAY -> {
                                        _state.update { absenceScreenState ->
                                            absenceScreenState.copy(delays = genericAbsences, loading = false)
                                        }
                                    }
                                    TypeOfAbsence.EXIT -> {
                                        _state.update { absenceScreenState ->
                                            absenceScreenState.copy(exits = genericAbsences, loading = false)
                                        }
                                    }
                                    TypeOfAbsence.UNKNOWN -> {}
                                }
                            }
                        }
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "Error while getting lessons data")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}