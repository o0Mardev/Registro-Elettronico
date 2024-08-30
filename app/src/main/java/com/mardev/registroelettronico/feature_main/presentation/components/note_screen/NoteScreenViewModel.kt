package com.mardev.registroelettronico.feature_main.presentation.components.note_screen

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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NoteScreenViewModel @Inject constructor(
    private val getNotes: GetNotes
) : ViewModel() {
    private val _state = MutableStateFlow(NoteScreenState())
    val state: StateFlow<NoteScreenState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getNotes().onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        result.data?.let { notes ->
                            _state.update { noteScreenState ->
                                noteScreenState.copy(
                                    allNotes = notes,
                                    filteredNotes = notes,
                                    loading = true
                                )
                            }
                        }

                    }

                    is Resource.Success -> {
                        result.data?.let { notes ->
                            _state.update { noteScreenState ->
                                noteScreenState.copy(
                                    allNotes = notes,
                                    filteredNotes = notes,
                                    loading = false
                                )
                            }
                        }
                    }

                    is Resource.Error -> {
                        Timber.e("Error while getting notes")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun updateNotesForSelectedTimeFraction(selectedTimeFraction: Int? = null) {
        _state.update { noteScreenState ->
            noteScreenState.copy(
                filteredNotes = if (selectedTimeFraction != null) noteScreenState.allNotes.filter { it.idTimeFraction == selectedTimeFraction } else {
                    noteScreenState.allNotes
                }
            )
        }

    }
}
