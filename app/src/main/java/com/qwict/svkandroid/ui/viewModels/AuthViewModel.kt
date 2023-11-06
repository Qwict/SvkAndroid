package com.qwict.svkandroid.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwict.svkandroid.common.AuthenticationSingleton
import com.qwict.svkandroid.data.local.saveEncryptedPreference
import com.qwict.svkandroid.data.remote.Api
import com.qwict.svkandroid.domain.use_cases.LoginUseCase
import com.qwict.svkandroid.ui.viewModels.states.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.inject.Inject

sealed interface AuthState {
    data object Idle : AuthState
    data object Loading : AuthState
    data object LoggedIn : AuthState
    data class Error(val message: String) : AuthState
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    var loginCredentials by mutableStateOf(LoginState())
        private set

    init {
        authState = if (AuthenticationSingleton.isUserAuthenticated) {
            AuthState.LoggedIn
        } else {
            AuthState.Idle
        }
    }

    fun login() {
        viewModelScope.launch {
            val body = buildJsonObject {
                put("email", JsonPrimitive(loginCredentials.email))
                put("password", JsonPrimitive(loginCredentials.password))
            }

            authState = AuthState.Loading

            authState = try {
                val response = Api.service.login(body)
                if (response.isEmpty()) {
                    AuthState.Error("Login failed")
                } else {
                    var token = response["token"]!!.jsonPrimitive.content
                    saveEncryptedPreference("token", token)
                    AuthenticationSingleton.validateUser()
                    AuthState.LoggedIn
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Failed to Login: ${e.message}")
                AuthState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun loginUser() {
        val email = loginCredentials.email
        val password = loginCredentials.password
        Log.i("AuthViewModel", "loginUser: $email, $password")
        loginUseCase(email, password).onEach { result ->
            throw NotImplementedError("loginUser() in AuthViewModel not implemented")
//          TODO: Future usecases should also be implemented in this way

//            when (result) {
//                is Resource.Success -> {
//                    authState = SvkAndroidUiState(user = result.data!!)
//                }
//
//                is Resource.Error -> {
//                    _state.value =
//                        SvkAndroidUiState(error = result.message ?: "An unexpected error occurred")
//                }
//
//                is Resource.Loading -> {
//                    _state.value = AuthState(isLoading = true)
//                }
//            }
        }.launchIn(viewModelScope)
    }

    fun logout() {
        AuthenticationSingleton.logout()
        loginCredentials = LoginState()
        authState = AuthState.Idle
    }

    fun updateLoginState(
        email: String? = null,
        password: String? = null,
    ) {
        email?.let {
            loginCredentials = loginCredentials.copy(email = it)
        }
        password?.let {
            loginCredentials = loginCredentials.copy(password = it)
        }
    }
}
