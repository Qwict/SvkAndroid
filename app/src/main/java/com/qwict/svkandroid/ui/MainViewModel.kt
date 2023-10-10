package com.qwict.svkandroid.ui

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.qwict.svkandroid.R
import com.qwict.svkandroid.data.SvkAndroidUiState
import com.qwict.svkandroid.dto.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SvkAndroidUiState())
    val uiState: StateFlow<SvkAndroidUiState> = _uiState.asStateFlow()
    var user by mutableStateOf(User())

//    fun setSelectedPlanet(planet: Planet) {
//        _uiState.update { currentState ->
//            currentState.copy(selectedPlanet = planet)
//        }
//    }

    //    For Auth0 to work
    var appJustLaunched by mutableStateOf(true)
    var userIsAuthenticated by mutableStateOf(false)

    private val TAG = "MainViewModel"
    private lateinit var account: Auth0
    private lateinit var context: Context

//    TODO: Possible auth0 implementation
    fun login() {
        WebAuthProvider
            .login(account)
            .withScheme(context.getString(R.string.com_auth0_scheme))
            .start(
                context,
                object : Callback<Credentials, AuthenticationException> {
                    override fun onFailure(error: AuthenticationException) {
                        // The user either pressed the “Cancel” button
                        // on the Universal Login screen or something
                        // unusual happened.
                        Log.e(TAG, "Error occurred in login(): $error")
                    }

                    override fun onSuccess(result: Credentials) {
                        // The user successfully logged in.
                        val idToken = result.idToken

                        // TODO: 🚨 REMOVE BEFORE GOING TO PRODUCTION!
                        Log.d(TAG, "ID token: $idToken")
                        user = User(idToken)

                        userIsAuthenticated = true
                        appJustLaunched = false
                    }
                },
            )
    }

    fun logout() {
        WebAuthProvider
            .logout(account)
            .withScheme(context.getString(R.string.com_auth0_scheme))
            .start(
                context,
                object : Callback<Void?, AuthenticationException> {

                    override fun onFailure(error: AuthenticationException) {
                        // For some reason, logout failed.
                        Log.e(TAG, "Error occurred in logout(): $error")
                    }

                    override fun onSuccess(result: Void?) {
                        // The user successfully logged out.
                        user = User()
                        userIsAuthenticated = false
                    }
                },
            )
    }

    fun setContext(activityContext: Context) {
        context = activityContext
        account = Auth0(
            context.getString(R.string.com_auth0_client_id),
            context.getString(R.string.com_auth0_domain),
        )
    }
}
