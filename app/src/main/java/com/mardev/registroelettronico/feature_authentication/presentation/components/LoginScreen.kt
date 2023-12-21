package com.mardev.registroelettronico.feature_authentication.presentation.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mardev.registroelettronico.core.presentation.AppState
import com.mardev.registroelettronico.feature_authentication.presentation.LoginViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController, appState: AppState
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val context = LocalContext.current
    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is LoginViewModel.UIEvent.ShowSnackBar -> {
                    appState.showSnackbar(
                        message = event.uiText.asString(
                            context
                        )
                    )
                }

                is LoginViewModel.UIEvent.NavigateToRoute -> {
                    navController.navigate(event.route)
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(appState.snackbarHostState) }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(value = state.taxCode,
                    placeholder = { Text(text = "Codice fiscale della scuola") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { newText ->
                        viewModel.onTaxCodeChange(newText)
                    })
                TextField(value = state.userName,
                    placeholder = { Text(text = "Codice utente") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { newText ->
                        viewModel.onUserNameChange(newText)
                    })

                var passwordVisible by rememberSaveable { mutableStateOf(false) }
                TextField(value = state.password,
                    placeholder = { Text(text = "Password") },
                    onValueChange = { newText ->
                        viewModel.onPasswordChange(newText)
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, null)
                        }
                    })

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Ricorda credenziali di accesso")
                    Checkbox(checked = state.isChecked, onCheckedChange = { isChecked ->
                        viewModel.onCheckedChange(isChecked);
                    })

                }

                Spacer(modifier = Modifier.height(30.dp))

                if (state.isLoading){
                    LinearProgressIndicator()
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(50.dp),
                    onClick = {
                        viewModel.onLogin()
                    }) {
                    Text(text = "Login")
                }

            }
        }
    }
}