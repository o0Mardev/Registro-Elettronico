package com.mardev.registroelettronico.feature_main.presentation.components.communication_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.use_case.GetCommunications
import com.mardev.registroelettronico.feature_main.domain.use_case.SetCommunicationRead
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
class CommunicationScreenViewModel @Inject constructor(
    private val getCommunications: GetCommunications,
    private val setCommunicationRead: SetCommunicationRead
) : ViewModel() {

    private val _state = MutableStateFlow(CommunicationScreenState())
    val state: StateFlow<CommunicationScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getCommunications().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { communicationScreenState ->
                            communicationScreenState.copy(
                                communications = result.data ?: emptyList(),
                                loading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update { communicationScreenState ->
                            communicationScreenState.copy(
                                communications = result.data ?: emptyList(),
                                loading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        Timber.e("Error while getting communications")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    //FIX COMMUNICATION READ CONFIRMATION DOESN'T WORK
    //NOTE THIS PROBLEM IT'S PRESENT ALSO IN THE OFFICIAL APP (NOT MY PROBLEM)
    fun onCommunicationItemClick(communicationId: Int, studentId: Int) {
        viewModelScope.launch {
            when(val result = setCommunicationRead(communicationId, studentId)) {
                is Resource.Success -> {
                    _state.update { communicationScreenState ->
                        communicationScreenState.copy(
                            loading = false,
                            communications = communicationScreenState.communications.map { communication ->
                                if (communication.id == communicationId){
                                    communication.copy(read = true)
                                } else communication
                            }
                        )
                    }

                }
                is Resource.Error -> {
                    Timber.e("Error while sending read confirmation")
                }
                is Resource.Loading -> {
                    Timber.i("Loading read confirmation")
                }
            }
        }
    }

}
