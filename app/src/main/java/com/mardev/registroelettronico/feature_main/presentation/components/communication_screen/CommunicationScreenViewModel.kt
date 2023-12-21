package com.mardev.registroelettronico.feature_main.presentation.components.communication_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.use_case.GetCommunications
import com.mardev.registroelettronico.feature_main.domain.use_case.SetCommunicationRead
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunicationScreenViewModel @Inject constructor(
    private val getCommunications: GetCommunications,
    private val setCommunicationRead: SetCommunicationRead
) : ViewModel() {

    private val _state = mutableStateOf(CommunicationScreenState())
    val state: State<CommunicationScreenState> = _state

    init {
        viewModelScope.launch {
            onGetCommunications()
        }
    }

    private suspend fun onGetCommunications(){
        viewModelScope.launch {
            getCommunications().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            communications = result.data?.second ?: emptyList(),
                            loading = true
                        )
                        Log.d("TAG", "Loading communications data")
                    }

                    is Resource.Success -> {
                        Log.d("TAG", "Got communications data")
                        _state.value = _state.value.copy(
                            communications = result.data?.second ?: emptyList(),
                            studentId = result.data?.first,
                            loading = false
                        )
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "onButtonClick: Error while getting data")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onCommunicationItemClick(communicationId: Int) {
        viewModelScope.launch {
            setCommunicationRead(communicationId, state.value.studentId)
        }
    }

}
