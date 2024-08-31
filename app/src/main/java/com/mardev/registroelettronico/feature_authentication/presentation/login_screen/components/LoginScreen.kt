package com.mardev.registroelettronico.feature_authentication.presentation.login_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mardev.registroelettronico.feature_authentication.presentation.login_screen.LoginViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    navController: NavController, retrievedTaxCode: String?
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()


    LaunchedEffect(key1 = true) {
        retrievedTaxCode?.let { viewModel.onTaxCodeChange(it) }

        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is LoginViewModel.UIEvent.NavigateToRoute -> {
                    if (navController.currentDestination?.route !== event.route) {
                        navController.navigate(event.route)
                    }
                }
            }
        }
    }

    Scaffold { paddingValues ->

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
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.onSearchClick()
                        }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        }
                    },
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

                TextField(value = state.password,
                    placeholder = { Text(text = "Password") },
                    onValueChange = { newText ->
                        viewModel.onPasswordChange(newText)
                    },
                    visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        val image = if (state.isPasswordVisible) Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        IconButton(onClick = { viewModel.onPasswordVisibilityClick() }) {
                            Icon(imageVector = image, null)
                        }
                    })

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Ricorda credenziali di accesso")
                    Checkbox(checked = state.isChecked, onCheckedChange = { isChecked ->
                        viewModel.onCheckedChange(isChecked)
                    })

                }

                Spacer(modifier = Modifier.height(30.dp))

                if (state.isLoading) {
                    LinearProgressIndicator()
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .width(250.dp)
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