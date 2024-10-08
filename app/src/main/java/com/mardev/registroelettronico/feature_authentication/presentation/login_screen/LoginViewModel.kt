package com.mardev.registroelettronico.feature_authentication.presentation.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mardev.registroelettronico.R
import com.mardev.registroelettronico.core.presentation.SnackbarAction
import com.mardev.registroelettronico.core.presentation.SnackbarController
import com.mardev.registroelettronico.core.presentation.SnackbarEvent
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.core.util.UIText
import com.mardev.registroelettronico.feature_authentication.domain.repository.RememberMe
import com.mardev.registroelettronico.feature_authentication.domain.repository.SessionCache
import com.mardev.registroelettronico.feature_authentication.domain.use_case.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val login: LoginUseCase,
    private val sessionCache: SessionCache,
    private val rememberMe: RememberMe
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        viewModelScope.launch {
            runBlocking {
                val isChecked = rememberMe.getCheckboxState()
                isChecked?.let {
                    _state.update { loginState ->
                        loginState.copy(isChecked = it)
                    }
                }

                if (isChecked != null && isChecked) {
                    val savedTaxCode = async { rememberMe.getTaxCode() }
                    val savedUsername = async { rememberMe.getUsername() }
                    val savedPassword = async { rememberMe.getPassword() }

                    _state.update { loginState ->
                        loginState.copy(
                            taxCode = savedTaxCode.await() ?: "",
                            userName = savedUsername.await() ?: "",
                            password = savedPassword.await() ?: ""
                        )
                    }
                }
            }

        }
    }

    fun onTaxCodeChange(newValue: String) {
        _state.update { loginState ->
            loginState.copy(
                taxCode = newValue
            )
        }
    }

    fun onSearchClick() {
        viewModelScope.launch {
            _eventFlow.emit(
                UIEvent.NavigateToRoute("search")
            )
        }
    }


    fun onUserNameChange(newValue: String) {
        _state.update { loginState ->
            loginState.copy(
                userName = newValue
            )
        }
    }


    fun onPasswordChange(newValue: String) {
        _state.update { loginState ->
            loginState.copy(
                password = newValue
            )
        }
    }

    fun onPasswordVisibilityClick() {
        _state.update { loginState ->
            loginState.copy(
                isPasswordVisible = !_state.value.isPasswordVisible
            )
        }
    }

    fun onLogin() {
        login(_state.value.taxCode, _state.value.userName, _state.value.password).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update { loginState ->
                        loginState.copy(
                            isLoading = false,
                        )
                    }

                    result.data?.let {
                        viewModelScope.launch {
                            if (it.session != null) {
                                sessionCache.saveSession(
                                    session = it.session
                                )

                                sessionCache.saveTaxCode(_state.value.taxCode)

                                showSnackbar(UIText.DynamicString("Ciao ${it.session.user.name}"))

                                _eventFlow.emit(
                                    UIEvent.NavigateToRoute("mainGraph")
                                )

                                if (_state.value.isChecked) {
                                    rememberMe.saveTaxcode(_state.value.taxCode)
                                    rememberMe.saveUsername(_state.value.userName)
                                    rememberMe.savePassword(_state.value.password)
                                }

                            } else {
                                showSnackbar(UIText.DynamicString(result.data.errorMessage))
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    _state.update { loginState ->
                        loginState.copy(
                            isLoading = false
                        )
                    }

                    showSnackbar(
                        result.uiText ?: UIText.StringResource(R.string.error4),
                        action = SnackbarAction(name = "Continua offline", action = {
                            viewModelScope.launch {
                                _eventFlow.emit(
                                    UIEvent.NavigateToRoute("mainGraph")
                                )
                            }
                        })
                    )
                }

                is Resource.Loading -> {
                    _state.update { loginState ->
                        loginState.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onCheckedChange(isChecked: Boolean) {
        _state.update { loginState -> loginState.copy(isChecked = isChecked) }
        viewModelScope.launch {
            rememberMe.saveCheckboxState(isChecked)
        }
    }


    sealed class UIEvent {
        data class NavigateToRoute(val route: String) : UIEvent()
    }


    private fun showSnackbar(message: UIText, action: SnackbarAction? = null) {
        viewModelScope.launch {
            SnackbarController.sendEvent(
                event = SnackbarEvent(message = message, action = action)
            )
        }
    }

}