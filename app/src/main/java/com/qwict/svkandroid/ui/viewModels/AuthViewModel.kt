package com.qwict.svkandroid.ui.viewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwict.svkandroid.common.AuthenticationSingleton
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.domain.use_cases.LoginUseCase
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
) : ViewModel() {
    var loginCredentialsUiState by mutableStateOf(LoginUiState())
        private set

    private var _authUiState = mutableStateOf(AuthUiState())
    val authUiState: State<AuthUiState> = _authUiState

    init {
        if (AuthenticationSingleton.isUserAuthenticated) {
            _authUiState.value = AuthUiState(
                user = AuthenticationSingleton.user,
                authState = AuthState.LoggedIn,
            )
        } else {
            _authUiState.value = AuthUiState(
                authState = AuthState.Idle,
            )
        }
    }

//    fun login() {
//        viewModelScope.launch {
//            val body = buildJsonObject {
//                put("email", JsonPrimitive(loginCredentials.email))
//                put("password", JsonPrimitive(loginCredentials.password))
//            }
//
//            authState = AuthState.Loading
//
//            authState = try {
//                val response = Api.service.login(body)
//                if (response.isEmpty()) {
//                    AuthState.Error("Login failed")
//                } else {
//                    var token = response["token"]!!.jsonPrimitive.content
//                    saveEncryptedPreference("token", token)
//                    AuthenticationSingleton.validateUser()
//                    AuthState.LoggedIn
//                }
//            } catch (e: Exception) {
//                Log.e("MainViewModel", "Failed to Login: ${e.message}")
//                AuthState.Error(e.localizedMessage ?: "Unknown error")
//            }
//        }
//    }

    fun login() {
        val email = loginCredentialsUiState.email
        val password = loginCredentialsUiState.password
        Log.i("AuthViewModel", "loginUser: $email, $password")
        loginUseCase(email, password).onEach { result ->
//          TODO: Future use cases "should" also be implemented in this way
            when (result) {
                is Resource.Success -> {
                    _authUiState.value = AuthUiState(user = result.data!!, authState = AuthState.LoggedIn)
                }

                is Resource.Error -> {
                    _authUiState.value = AuthUiState(error = result.message ?: "An unexpected error occurred", authState = AuthState.Idle)
                }

                is Resource.Loading -> {
                    _authUiState.value = AuthUiState(authState = AuthState.Loading)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun logout() {
        AuthenticationSingleton.logout()
        // Will remember the email address on logout
        loginCredentialsUiState = loginCredentialsUiState.copy(password = "")
        // Clear the authentication ui sate on logout
        _authUiState.value = AuthUiState(
            authState = AuthState.Idle,
        )
    }

    fun updateLoginState(
        email: String? = null,
        password: String? = null,
    ) {
        email?.let {
            loginCredentialsUiState = loginCredentialsUiState.copy(email = it)
        }
        password?.let {
            loginCredentialsUiState = loginCredentialsUiState.copy(password = it)
        }
    }
}
