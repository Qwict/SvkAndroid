package com.qwict.svkandroid.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Person
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
import com.qwict.svkandroid.ui.components.AlertDialog
import com.qwict.svkandroid.ui.components.AuthButton
import com.qwict.svkandroid.ui.components.Loading
import com.qwict.svkandroid.ui.components.ShakingPasswordTextField
import com.qwict.svkandroid.ui.components.ShakingTextFieldWithIcon
import com.qwict.svkandroid.ui.components.animateText
import com.qwict.svkandroid.ui.viewModels.AuthenticationFormEvent
import com.qwict.svkandroid.ui.viewModels.states.AuthFormState
import com.qwict.svkandroid.ui.viewModels.states.AuthUiState

/**
 * Composable function to display the registration screen.
 *
 * @param authState Authentication UI state.
 * @param authFormState Authentication form state.
 * @param registerUser Callback function for registering a new user.
 * @param onEvent Callback function for handling authentication form events.
 * @param switchPasswordVisibility Callback function for toggling password visibility.
 * @param clearValidationErrors Callback function for clearing validation errors.
 * @param navigateToLoginScreen Callback function for navigating to the login screen.
 */
@Composable
fun RegisterScreen(
    authState: AuthUiState,
    authFormState: AuthFormState,
    registerUser: () -> Unit,
    onEvent: (AuthenticationFormEvent) -> Unit,
    switchPasswordVisibility: () -> Unit,
    clearValidationErrors: () -> Unit,
    navigateToLoginScreen: () -> Unit,
) {
    if (authState.isLoading) {
        Loading()
    } else {
        // For shaking text fields
        val coroutineScope = rememberCoroutineScope()
        val view = LocalView.current
        val offsetXEmail = remember { Animatable(0f) }
        val offsetXPassword = remember { Animatable(0f) }
        val offsetXConfirmPassword = remember { Animatable(0f) }

        BackHandler(onBack = {
            clearValidationErrors()
            navigateToLoginScreen()
        })

        if (authState.isRequestSendDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    clearValidationErrors()
                },
                onConfirmation = {
                    clearValidationErrors()
                    navigateToLoginScreen()
                },
                dialogTitle = stringResource(R.string.registration_successful_title),
                dialogText = stringResource(R.string.you_have_successfully_requested_awaiting_txt),
                icon = Icons.Default.Person,
            )
        }

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
                    text = stringResource(R.string.new_account_title),
                    style = MaterialTheme.typography.displayMedium,
                )

                ShakingTextFieldWithIcon(
                    textFieldValue = authFormState.email,
                    onValueChange = { onEvent(AuthenticationFormEvent.EmailChanged(it)) },
                    label = stringResource(R.string.reg_email_txt_fld),
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
                    onValueChange = { onEvent(AuthenticationFormEvent.PasswordChanged(it)) },
                    label = stringResource(R.string.reg_password_txt_fld),
                    isError = authFormState.passwordError.isNotEmpty(),
                    errorText = authFormState.passwordError,
                    offsetX = offsetXPassword,
                    leadingIcon = Icons.Default.Lock,
                    authState = authState,
                    switchPasswordVisibility = { switchPasswordVisibility() },
                )

                ShakingPasswordTextField(
                    textFieldValue = authFormState.passwordConfirm,
                    onValueChange = { onEvent(AuthenticationFormEvent.ConfirmPasswordChanged(it)) },
                    label = stringResource(R.string.confirm_password_txt_fld),
                    isError = authFormState.passwordConfirmError.isNotEmpty(),
                    errorText = authFormState.passwordConfirmError,
                    offsetX = offsetXConfirmPassword,
                    leadingIcon = Icons.Default.LockReset,
                    authState = authState,
                    switchPasswordVisibility = { switchPasswordVisibility() },
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = authState.error,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp),
                )
                AuthButton(
                    text = stringResource(R.string.submit_btn),
                    onClick = {
                        registerUser()
                        if (authFormState.emailError.isNotEmpty()) {
                            animateText(offsetXEmail, coroutineScope, view)
                        }
                        if (authFormState.passwordError.isNotEmpty()) {
                            animateText(offsetXPassword, coroutineScope, view)
                        }
                        if (authFormState.passwordConfirmError.isNotEmpty()) {
                            animateText(offsetXConfirmPassword, coroutineScope, view)
                        }
                    },
                )
            }
        }
    }
}
