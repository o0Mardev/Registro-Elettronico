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
                        Log.d("TAG", "Loading communications data")
                    }

                    is Resource.Success -> {
                        Log.d("TAG", "Got communications data")
                        _state.update { communicationScreenState ->
                            communicationScreenState.copy(
                                communications = result.data ?: emptyList(),
                                loading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "onButtonClick: Error while getting data")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onCommunicationItemClick(communicationId: Int, studentId: Int) {
        viewModelScope.launch {
            val result = setCommunicationRead(communicationId, studentId)
            when(result) {
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
                    Log.d("TAG", "onCommunicationItemClick: Error ${result.uiText}")
                }
                is Resource.Loading -> {

                }
            }
        }
    }

}
