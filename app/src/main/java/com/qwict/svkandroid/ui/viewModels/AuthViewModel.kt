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
import com.qwict.svkandroid.domain.use_cases.RegisterUseCase
import com.qwict.svkandroid.domain.validator.Validators
import com.qwict.svkandroid.ui.viewModels.states.AuthFormState
import com.qwict.svkandroid.ui.viewModels.states.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * ViewModel for managing authentication-related logic and data.
 *
 * This ViewModel is annotated with [HiltViewModel] for Dagger-Hilt integration.
 *
 * @property loginUseCase The use case responsible for handling user login.
 * @property registerUseCase The use case responsible for handling user registration.
 * @property validators The validators used for input validation.
 * @constructor Creates an instance of [AuthViewModel].
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val validators: Validators,
) : ViewModel() {
    var authFormState by mutableStateOf(AuthFormState())
        private set
    var authUiState by mutableStateOf(AuthUiState())
        private set

    init {
        authUiState = if (AuthenticationSingleton.isUserAuthenticated) {
            AuthUiState(
                user = AuthenticationSingleton.user,
            )
        } else {
            AuthUiState()
        }
    }

    fun clearValidationErrors() {
        authFormState = authFormState.copy(
            emailError = "",
            passwordError = "",
            passwordConfirmError = "",
        )
        authUiState = authUiState.copy(
            error = "",
        )
    }

    /**
     * Initiates the user login process using the provided credentials.
     *
     * Validates the email and password, and triggers the [LoginUseCase].
     * Updates the [AuthUiState] based on the result of the login attempt.
     */
    fun login() {
        val email = authFormState.email
        val password = authFormState.password
        Log.i("AuthViewModel", "loginUser: $email, $password")

        val emailResult = validators.emailValidator(authFormState.email)
        val passwordResult = validators.passwordValidator(authFormState.password)

        Log.i("AuthViewModel", "loginUser: $emailResult, $passwordResult")
        val hasErrors = listOf(
            passwordResult,
            emailResult,
        ).any { !it.successful }

        if (hasErrors) {
            authFormState = authFormState.copy(
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
                            AuthUiState(user = result.data!!)
                    }

                    is Resource.Error -> {
                        authUiState = AuthUiState(
                            error = result.message ?: "An unexpected error occurred",
                        )
                    }

                    is Resource.Loading -> {
                        authUiState = authUiState.copy(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Initiates the user registration process using the provided registration details.
     *
     * Validates the email and password, triggers the [RegisterUseCase], and updates the [AuthUiState]
     * based on the result of the registration attempt.
     */
    fun registerUser() {
        val emailResult = validators.emailValidator(authFormState.email)
        val passwordResult =
            validators.validateNewPassword(authFormState.password, authFormState.passwordConfirm)

        val hasErrors = listOf(
            emailResult,
            passwordResult,
        ).any { !it.successful }

        if (hasErrors) {
            authFormState = authFormState.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                passwordConfirmError = passwordResult.errorMessage,
            )
            return
        } else {
            registerUseCase(authFormState.email, authFormState.password).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        authUiState =
                            AuthUiState(
                                user = result.data!!,
                                isRequestSendDialogVisible = true,
                            )
                    }

                    is Resource.Error -> {
                        authUiState = authUiState.copy(
                            error = result.message ?: "An unexpected error occurred",
                            isLoading = false,
                        )
                    }

                    is Resource.Loading -> {
                        authUiState = authUiState.copy(
                            error = "",
                            isLoading = true,
                        )
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    /**
     * Logs out the current user.
     *
     * Invokes the logout method from [AuthenticationSingleton] and clears sensitive information
     * from the [AuthFormState]. Additionally, resets the [AuthUiState] to its initial state.
     */
    fun logout() {
        AuthenticationSingleton.logout()
        // Will remember the email address on logout
        authFormState = authFormState.copy(password = "")
        // Clear the authentication ui sate on logout
    }

    /**
     * Toggles the visibility of the password input field.
     *
     * Updates the [AuthUiState] to change the visibility state of the password input field.
     */
    fun switchPasswordVisibility() {
        authUiState = authUiState.copy(isPasswordVisible = !authUiState.isPasswordVisible)
    }

    /**
     * Handles updates to the authentication form state based on the provided [AuthenticationFormEvent].
     *
     * @param event The event representing a change in the authentication form.
     */
    fun onUpdateLoginState(event: AuthenticationFormEvent) {
        when (event) {
            is AuthenticationFormEvent.EmailChanged -> {
                authFormState = authFormState.copy(email = event.email)
            }
            is AuthenticationFormEvent.PasswordChanged -> {
                authFormState = authFormState.copy(password = event.password)
            }
            is AuthenticationFormEvent.ConfirmPasswordChanged -> {
                authFormState = authFormState.copy(passwordConfirm = event.passwordConfirm)
            }
        }
    }
}

/**
 * Sealed class representing events that trigger changes in the authentication form state.
 */
sealed class AuthenticationFormEvent {
    data class EmailChanged(val email: String) : AuthenticationFormEvent()
    data class PasswordChanged(val password: String) : AuthenticationFormEvent()
    data class ConfirmPasswordChanged(val passwordConfirm: String) : AuthenticationFormEvent()
}
