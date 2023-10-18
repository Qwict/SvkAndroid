package com.qwict.svkandroid.ui

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.qwict.svkandroid.api.IUserApi
import com.qwict.svkandroid.data.SvkAndroidUiState
import com.qwict.svkandroid.dto.User
import com.qwict.svkandroid.helper.RetrofitSingleton
import com.qwict.svkandroid.helper.clearEncryptedPreferences
import com.qwict.svkandroid.helper.saveEncryptedPreference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SvkAndroidUiState())
    val uiState: StateFlow<SvkAndroidUiState> = _uiState.asStateFlow()
    var user by mutableStateOf(User())
    var jwt: String = ""
    val snackbarHostState = SnackbarHostState()

    var appJustLaunched by mutableStateOf(true)
    var userIsAuthenticated by mutableStateOf(false)

    private val TAG = "MainViewModel"
    private lateinit var context: Context

    fun login(email: MutableState<TextFieldValue>, password: MutableState<TextFieldValue>): Boolean {
        val apiService = RetrofitSingleton.getRetro().create(IUserApi::class.java)
        var success = false
        val body = mapOf(
            "email" to email.value.text,
            "password" to password.value.text,
        )
        Log.i("MainViewModel", body.toString())

        apiService.login(body).enqueue(object :
            retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.i("MainViewModel", "onResponse: ${response.body()}")
                if (response.isSuccessful) {
                    Log.i("MainViewModel", "Logged in")
                    val stringResponse = response.body()?.string()
                    var json = JSONObject(stringResponse)
                    jwt = json.getString("token")
                    user = User(
                        jwt,
                    )
                    Log.i("MainViewModel", jwt)
                    userIsAuthenticated = true
//                    Not sure if this is needed (because this also happens in MainActivity onPause)
                    saveEncryptedPreference("token", user.token, context)
                    success = true
                } else {
                    Log.e("MainViewModel", "Failed to Login there was a response")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.i("MainViewModel", "onFailure: ${t.message}")
                Log.e("MainViewModel", "Failed to Login there was no response")
            }
        })
        return success
    }

    fun logout() {
        userIsAuthenticated = false
        user = User()
        clearEncryptedPreferences("token", context)
    }

    fun setContext(activityContext: Context) {
        context = activityContext
    }
}
