package com.mardev.registroelettronico.feature_main.presentation.components.note_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.feature_main.domain.use_case.GetNotes
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
class NoteScreenViewModel @Inject constructor(
    private val getNotes: GetNotes
) : ViewModel() {
    private val _state = MutableStateFlow(NoteScreenState())
    val state: StateFlow<NoteScreenState> = _state.asStateFlow()

    init {
        Log.d("TAG", "Init block")
        viewModelScope.launch {
            getNotes().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        Log.d("TAG", "Loading notes data")
                        result.data?.let { notes ->
                            _state.update { noteScreenState ->
                                noteScreenState.copy(notes = notes)
                            }
                        }

                    }

                    is Resource.Success -> {
                        Log.d("TAG", "Got notes data")
                        result.data?.let { notes ->
                            _state.update { noteScreenState ->
                                noteScreenState.copy(notes = notes, loading = false)
                            }
                        }
                    }

                    is Resource.Error -> {
                        Log.d("TAG", "Error while getting notes data")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}
