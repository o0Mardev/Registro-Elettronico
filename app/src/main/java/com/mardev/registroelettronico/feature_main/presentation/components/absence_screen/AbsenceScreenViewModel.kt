package com.mardev.registroelettronico.feature_main.presentation.components.absence_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.presentation.SnackbarController
import com.mardev.registroelettronico.core.presentation.SnackbarEvent
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.core.util.UIText
import com.mardev.registroelettronico.feature_main.domain.model.TypeOfAbsence
import com.mardev.registroelettronico.feature_main.domain.use_case.GetAbsences
import com.mardev.registroelettronico.feature_main.domain.use_case.GetTypesOfJustification
import com.mardev.registroelettronico.feature_main.domain.use_case.JustifyAbsence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AbsenceScreenViewModel @Inject constructor(
    private val getAbsences: GetAbsences,
    private val getTypesOfJustification: GetTypesOfJustification,
    private val justifyAbsence: JustifyAbsence
) : ViewModel() {
    private val _state = MutableStateFlow(AbsenceScreenState())
    val state: StateFlow<AbsenceScreenState> = _state.asStateFlow()

    init {
        updateAbsences()
        updateTypesOfJustification()
    }

    private fun updateTypesOfJustification() {
        viewModelScope.launch {
            getTypesOfJustification().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { absenceScreenState -> absenceScreenState.copy(loading = true) }
                    }

                    is Resource.Success -> {
                        _state.update { absenceScreenState ->
                            absenceScreenState.copy(
                                loading = false,
                                typesOfJustification = result.data ?: emptyList()
                            )
                        }
                    }

                    is Resource.Error -> {
                        Timber.e("Error while getting types of justification data")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun updateAbsences() {
        viewModelScope.launch {
            getAbsences().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        result.data?.let { genericAbsences ->
                            _state.update { absenceScreenState ->
                                absenceScreenState.copy(
                                    genericAbsences = genericAbsences,
                                    loading = true
                                )
                            }
                            val groupedGenericAbsences =
                                _state.value.genericAbsences.groupBy { it.typeOfAbsence }
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
                        result.data?.let { genericAbsences ->
                            _state.update { absenceScreenState ->
                                absenceScreenState.copy(
                                    genericAbsences = genericAbsences,
                                    loading = false
                                )
                            }
                            val groupedGenericAbsences =
                                _state.value.genericAbsences.groupBy { it.typeOfAbsence }
                            groupedGenericAbsences.forEach { (type, genericAbsences) ->
                                when (type) {
                                    TypeOfAbsence.ABSENCE -> {
                                        _state.update { absenceScreenState ->
                                            absenceScreenState.copy(
                                                absences = genericAbsences
                                            )
                                        }
                                    }

                                    TypeOfAbsence.DELAY -> {
                                        _state.update { absenceScreenState ->
                                            absenceScreenState.copy(
                                                delays = genericAbsences
                                            )
                                        }
                                    }

                                    TypeOfAbsence.EXIT -> {
                                        _state.update { absenceScreenState ->
                                            absenceScreenState.copy(
                                                exits = genericAbsences
                                            )
                                        }
                                    }

                                    TypeOfAbsence.UNKNOWN -> {}
                                }
                            }
                        }
                    }

                    is Resource.Error -> {
                        Timber.e("Error while getting lessons data")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onJustifyAbsenceClick(
        optionId: Int,
        absenceId: Int,
        typeOfAbsence: Char,
        pin: String,
        studentId: Int
    ) {
        Timber.d("onJustifyAbsenceClick: $optionId, $absenceId, $pin")

        viewModelScope.launch {
            when (val result = justifyAbsence(
                absenceId = absenceId,
                pin = pin,
                studentId = studentId,
                type = typeOfAbsence,
                option = optionId.toString()
            )) {
                is Resource.Success -> {
                    Timber.i("Justification attempt went ok")

                    //TODO Ricorda, ora la giustifica funziona, ma non viene subito aggiornata, questo sotto non sembra funzionare.
                    _state.update { absenceScreenState ->
                        absenceScreenState.copy(genericAbsences = absenceScreenState.genericAbsences.map { genericAbsence ->
                            if (genericAbsence.id == absenceId){
                                genericAbsence.copy(dateJustification = LocalDate.now())
                            } else genericAbsence
                        })
                    }
                }
                is Resource.Loading -> {
                    Timber.i("Loading justification attempt")
                }
                is Resource.Error -> {
                    Timber.e("Error while trying to justify: ${result.uiText}")
                    SnackbarController.sendEvent(
                        event = SnackbarEvent(message = result.uiText?: UIText.DynamicString("Errore"))
                    )
                }
            }
        }
    }
}