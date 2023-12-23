package com.qwict.svkandroid.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.components.AuthButton
import com.qwict.svkandroid.ui.components.Loading
import com.qwict.svkandroid.ui.components.ShakingPasswordTextField
import com.qwict.svkandroid.ui.components.ShakingTextFieldWithIcon
import com.qwict.svkandroid.ui.components.SvkOutlinedButton
import com.qwict.svkandroid.ui.components.animateText
import com.qwict.svkandroid.ui.viewModels.AuthenticationFormEvent
import com.qwict.svkandroid.ui.viewModels.states.AuthFormState
import com.qwict.svkandroid.ui.viewModels.states.AuthUiState

/**
 * Composable function representing the login screen of the application.
 *
 * @param onUpdateLoginState Callback to update the authentication form state.
 * @param login Callback to initiate the login process.
 * @param authFormState Current authentication form state.
 * @param authUiState Current authentication UI state.
 * @param switchPasswordVisibility Callback to toggle password visibility.
 * @param navigateToRegisterScreen Callback to navigate to the register screen.
 * @param clearValidationErrors Callback to clear validation errors.
 */
@Composable
fun LoginScreen(
    onUpdateLoginState: (AuthenticationFormEvent) -> Unit,
    login: () -> Unit,
    authFormState: AuthFormState,
    authUiState: AuthUiState,
    switchPasswordVisibility: () -> Unit,
    navigateToRegisterScreen: () -> Unit,
    clearValidationErrors: () -> Unit,
) {
    if (authUiState.isLoading) {
        Loading()
    } else {
        val coroutineScope = rememberCoroutineScope()
        val view = LocalView.current
        val offsetXEmail = remember { Animatable(0f) }
        val offsetXPassword = remember { Animatable(0f) }

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
                    text = stringResource(R.string.sign_in_title),
                    style = MaterialTheme.typography.displayMedium,
                )

                ShakingTextFieldWithIcon(
                    textFieldValue = authFormState.email,
                    onValueChange = { onUpdateLoginState(AuthenticationFormEvent.EmailChanged(it)) },
                    label = stringResource(R.string.email_txt_fld),
                    isError = authFormState.emailError.isNotEmpty(),
                    errorText = authFormState.emailError,
                    offsetX = offsetXEmail,
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                    ),
                )
                ShakingPasswordTextField(
                    textFieldValue = authFormState.password,
                    onValueChange = { onUpdateLoginState(AuthenticationFormEvent.PasswordChanged(it)) },
                    label = stringResource(R.string.password_txt_fld),
                    isError = authFormState.passwordError.isNotEmpty(),
                    errorText = authFormState.passwordError,
                    offsetX = offsetXPassword,
                    leadingIcon = Icons.Default.Lock,
                    authState = authUiState,
                    switchPasswordVisibility = { switchPasswordVisibility() },
                )
                Text(
                    text = authUiState.error,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp),
                )
                AuthButton(
                    text = stringResource(R.string.login_btn),
                    onClick = {
                        login()
                        if (authFormState.emailError.isNotEmpty()) {
                            animateText(offsetXEmail, coroutineScope, view)
                        }
                        if (authFormState.passwordError.isNotEmpty()) {
                            animateText(offsetXPassword, coroutineScope, view)
                        }
                    },
                )
                Spacer(modifier = Modifier.padding(4.dp))
                SvkOutlinedButton(
                    onClick = {
                        navigateToRegisterScreen()
                        clearValidationErrors()
                    },
                    text = stringResource(R.string.request_account_btn),
                )
            }
        }
    }
}
