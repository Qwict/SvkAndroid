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

//        Api.service.login(body).enqueue(object : retrofit2.Callback<JsonObject> {
//            @RequiresApi(Build.VERSION_CODES.O)
//            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                if (response.isSuccessful) {
//                    Log.i("MainViewModel", "Logged in")
// //                    user.token = json.getString("token")
//                    user = User(
//                        response.body()!!["token"].toString(),
//                    )
// //                    Not sure if this is needed (because this also happens in MainActivity onPause)
//                    // Save the token
//                    saveEncryptedPreference("token", user.token)
//                    // validate the user with this token, (will put the isUserAuthenticated to true)
//                    AuthenticationSingleton.validateUser()
//                    success = true
//                } else {
//                    Log.e("MainViewModel", "Failed to Login: ${response.errorBody()}")
//                }
//            }
//
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                Log.e("MainViewModel", "Failed to Login: ${t.message}")
//            }
//        }
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
