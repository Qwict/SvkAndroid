package com.qwict.svkandroid.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwict.svkandroid.api.Api
import com.qwict.svkandroid.common.AuthenticationSingleton
import com.qwict.svkandroid.data.LoginState
import com.qwict.svkandroid.data.saveEncryptedPreference
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive

sealed interface AuthState {
    object Idle : AuthState
    object Loading : AuthState
    object LoggedIn : AuthState
    data class Error(val message: String) : AuthState
}

class AuthViewModel : ViewModel() {


    var authState by mutableStateOf<AuthState>(AuthState.Idle)
        private set
    var loginCredentials by mutableStateOf(LoginState())
        private set

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

    fun logout() {
        AuthenticationSingleton.logout()
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
