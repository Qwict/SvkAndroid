package com.qwict.svkandroid.ui

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.qwict.svkandroid.api.Api
import com.qwict.svkandroid.common.AuthenticationSingleton
import com.qwict.svkandroid.common.AuthenticationSingleton.validateUser
import com.qwict.svkandroid.data.LoginState
import com.qwict.svkandroid.data.SvkAndroidUiState
import com.qwict.svkandroid.data.saveEncryptedPreference
import com.qwict.svkandroid.dto.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SvkAndroidUiState())
    val uiState: StateFlow<SvkAndroidUiState> = _uiState.asStateFlow()
    var user by mutableStateOf(User())
    val snackbarHostState = SnackbarHostState()

    val currentBarcode = mutableStateOf("")
    var loginState = mutableStateOf(LoginState())
        private set
    val email = mutableStateOf(TextFieldValue())
    val password = mutableStateOf(TextFieldValue())

    var appJustLaunched by mutableStateOf(true)

    var laadBonnen = mutableListOf<String>()

    private val TAG = "MainViewModel"
    private lateinit var context: Context

    fun login(): Boolean {
        var success = false
        val body = buildJsonObject {
            put("email", email.value.text)
            put("password", password.value.text)
        }
        Api.service.login(body).enqueue(object : retrofit2.Callback<JsonObject> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.i("MainViewModel", "Logged in")
//                    user.token = json.getString("token")
                    user = User(
                        response.body()!!["token"].toString(),
                    )
//                    Not sure if this is needed (because this also happens in MainActivity onPause)
                    // Save the token
                    saveEncryptedPreference("token", user.token)
                    // validate the user with this token, (will put the isUserAuthenticated to true)
                    validateUser()
                    success = true
                } else {
                    Log.e("MainViewModel", "Failed to Login: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.e("MainViewModel", "Failed to Login: ${t.message}")
            }
        })
        return success
    }

    fun logout() {
        AuthenticationSingleton.logout()
    }

    fun setContext(activityContext: Context) {
        context = activityContext
    }

    fun updateLoginState(
        email: String? = null,
        password: String? = null,
    ) {
        email?.let {
            loginState.value = loginState.value.copy(email = it)
        }
        password?.let {
            loginState.value = loginState.value.copy(password = it)
        }
    }
}
