package com.qwict.svkandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qwict.svkandroid.ui.components.Loading
import com.qwict.svkandroid.ui.viewModels.AuthenticationFormEvent
import com.qwict.svkandroid.ui.viewModels.states.AuthUiState
import com.qwict.svkandroid.ui.viewModels.states.LoginUiState

@Composable
fun AuthenticationScreen(
    onUpdateLoginState: (AuthenticationFormEvent) -> Unit,
    login: () -> Unit,
    loginUiState: LoginUiState,
    authUiState: AuthUiState,
    switchPasswordVisibility: () -> Unit,
) {
    if (authUiState.isLoading) {
        Loading()
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Sign in",
                    style = MaterialTheme.typography.displayMedium,
                )

                LoginInputFields(
                    loginUiState = loginUiState,
                    onUpdateLoginState = onUpdateLoginState,
                    authUiState = authUiState,
                    switchPasswordVisibility = switchPasswordVisibility,
                )

                Text(
                    text = authUiState.error,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp),
                )
                LogButton(
                    text = "Login",
                    onClick = { login() },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginInputFields(
    loginUiState: LoginUiState,
    onUpdateLoginState: (AuthenticationFormEvent) -> Unit,
    authUiState: AuthUiState,
    switchPasswordVisibility: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = loginUiState.email,
            onValueChange = { onUpdateLoginState(AuthenticationFormEvent.EmailChanged(it)) },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
            ),
            isError = authUiState.emailError.isNotEmpty(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
        )
        if (authUiState.emailError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = authUiState.emailError,
                color = MaterialTheme.colorScheme.error,
            )
        } else {
            Spacer(modifier = Modifier.height(20.dp))
        }

        OutlinedTextField(
            value = loginUiState.password,
            onValueChange = { onUpdateLoginState(AuthenticationFormEvent.PasswordChanged(it)) },
            label = { Text("Password") },
            visualTransformation = if (authUiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            isError = authUiState.passwordError.isNotEmpty(),
            trailingIcon = {
                val image = if (authUiState.passwordVisible) {
                    Icons.Filled.Visibility
                } else {
                    Icons.Filled.VisibilityOff
                }
                val description = if (authUiState.passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { switchPasswordVisibility() }) {
                    Icon(imageVector = image, description)
                }
            },
        )
        if (authUiState.passwordError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = authUiState.passwordError,
                color = MaterialTheme.colorScheme.error,
            )
        } else {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun LogButton(
    text: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
            )
        }
    }
}
