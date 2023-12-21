package com.mardev.registroelettronico.feature_authentication.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.core.util.UIText
import com.mardev.registroelettronico.feature_authentication.domain.repository.RememberMe
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_authentication.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val login: LoginUseCase,
    private val sessionCache: SessionCache,
    private val rememberMe: RememberMe
) : ViewModel() {

    private val _state = mutableStateOf(LoginState())
    val state = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        viewModelScope.launch {
            runBlocking {
                rememberMe.getCheckboxState()?.let {
                    _state.value = _state.value.copy(isChecked = it)
                }

                if (_state.value.isChecked){
                    val savedTaxCode = async { rememberMe.getTaxCode() }
                    val savedUsername = async { rememberMe.getUsername() }
                    val savedPassword = async { rememberMe.getPassword() }

                    _state.value = _state.value.copy(
                        taxCode = savedTaxCode.await() ?: "",
                        userName = savedUsername.await() ?: "",
                        password = savedPassword.await() ?: ""
                    )
                }
            }

        }
    }

    fun onTaxCodeChange(newValue: String) {
        _state.value = _state.value.copy(
            taxCode = newValue
        )
    }


    fun onUserNameChange(newValue: String) {
        _state.value = _state.value.copy(
            userName = newValue
        )
    }


    fun onPasswordChange(newValue: String) {
        _state.value = _state.value.copy(
            password = newValue
        )
    }

    fun onLogin() {
        login(_state.value.taxCode, _state.value.userName, _state.value.password).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                    )

                    result.data?.let {
                        viewModelScope.launch {
                            if (it.session != null) {
                                sessionCache.saveSession(
                                    session = it.session
                                )
//                                _eventFlow.emit(
//                                    UIEvent.ShowSnackBar(
//                                        UIText.DynamicString("Login successfully")
//                                    )
//                                )
                                _eventFlow.emit(
                                    UIEvent.ShowSnackBar(
                                        UIText.DynamicString("Benvenuto " + it.session.user.name)
                                    )
                                )
                                _eventFlow.emit(
                                    UIEvent.NavigateToRoute("mainGraph")
                                )

                                if (_state.value.isChecked){
                                    rememberMe.saveTaxcode(_state.value.taxCode)
                                    rememberMe.saveUsername(_state.value.userName)
                                    rememberMe.savePassword(_state.value.password)
                                }

                            } else {
                                _eventFlow.emit(
                                    UIEvent.ShowSnackBar(
                                        UIText.DynamicString(result.data.errorMessage)
                                    )
                                )
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(
                        UIEvent.ShowSnackBar(
                            result.uiText ?: UIText.DynamicString("Unknown error")
                        )
                    )
                    _eventFlow.emit(
                        UIEvent.ShowSnackBar(
                            UIText.DynamicString("Visualizzazione offline")
                        )
                    )
                    _eventFlow.emit(
                        UIEvent.NavigateToRoute("mainGraph")
                    )
                }

                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onCheckedChange(isChecked: Boolean) {
        _state.value = _state.value.copy(isChecked = isChecked)
        viewModelScope.launch {
            rememberMe.saveCheckboxState(isChecked)
        }
    }


    sealed class UIEvent {
        data class ShowSnackBar(val uiText: UIText) : UIEvent()
        data class NavigateToRoute(val route: String) : UIEvent()
    }

}