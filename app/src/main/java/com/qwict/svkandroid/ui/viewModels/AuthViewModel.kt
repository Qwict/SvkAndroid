package com.qwict.svkandroid.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwict.svkandroid.common.AuthenticationSingleton
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.domain.use_cases.LoginUseCase
import com.qwict.svkandroid.domain.validator.Validators
import com.qwict.svkandroid.ui.viewModels.states.AuthState
import com.qwict.svkandroid.ui.viewModels.states.AuthUiState
import com.qwict.svkandroid.ui.viewModels.states.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val validators: Validators,
) : ViewModel() {
    var loginUiState by mutableStateOf(LoginUiState())
        private set
    var authUiState by mutableStateOf(AuthUiState())
        private set

    init {
        authUiState = if (AuthenticationSingleton.isUserAuthenticated) {
            AuthUiState(
                user = AuthenticationSingleton.user,
                authState = AuthState.LoggedIn,
            )
        } else {
            AuthUiState(
                authState = AuthState.UnAuthenticated,
            )
        }
    }

    fun login() {
        val email = loginUiState.email
        val password = loginUiState.password
        Log.i("AuthViewModel", "loginUser: $email, $password")

        val emailResult = validators.emailValidator(loginUiState.email)
        val passwordResult = validators.passwordValidator(loginUiState.password)

        Log.i("AuthViewModel", "loginUser: $emailResult, $passwordResult")
        val hasErrors = listOf(
            passwordResult,
            emailResult,
        ).any { !it.successful }

        if (hasErrors) {
            authUiState = authUiState.copy(
                passwordError = passwordResult.errorMessage,
                emailError = emailResult.errorMessage,
            )
            return
        } else {
            loginUseCase(email, password).onEach { result ->
//          TODO: Future use cases "should" also be implemented in this way
                when (result) {
                    is Resource.Success -> {
                        authUiState =
                            AuthUiState(user = result.data!!, authState = AuthState.LoggedIn)
                    }

                    is Resource.Error -> {
                        authUiState = AuthUiState(
                            error = result.message ?: "An unexpected error occurred",
                            authState = AuthState.UnAuthenticated,
                        )
                    }

                    is Resource.Loading -> {
                        authUiState = authUiState.copy(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun logout() {
        AuthenticationSingleton.logout()
        // Will remember the email address on logout
        loginUiState = loginUiState.copy(password = "")
        // Clear the authentication ui sate on logout
        authUiState = AuthUiState(
            authState = AuthState.UnAuthenticated,
        )
    }

    fun onUpdateLoginState(event: AuthenticationFormEvent) {
        when (event) {
            is AuthenticationFormEvent.EmailChanged -> {
                loginUiState = loginUiState.copy(email = event.email)
            }
            is AuthenticationFormEvent.PasswordChanged -> {
                loginUiState = loginUiState.copy(password = event.password)
            }
            else -> {}
        }
    }
}

sealed class AuthenticationFormEvent {
    data class EmailChanged(val email: String) : AuthenticationFormEvent()
    data class PasswordChanged(val password: String) : AuthenticationFormEvent()
}
