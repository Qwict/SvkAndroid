package com.qwict.svkandroid.common

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.qwict.svkandroid.common.Constants.EMPTY_USER
import com.qwict.svkandroid.data.local.getEncryptedPreference
import com.qwict.svkandroid.data.local.removeEncryptedPreference
import com.qwict.svkandroid.domain.model.User

object AuthenticationSingleton {
    var isUserAuthenticated by mutableStateOf(false)
        private set
    var user by mutableStateOf(EMPTY_USER)
        private set

    fun validateUser() {
        val token = getEncryptedPreference("token")
        if (token != null && token != "") {
            if (tokenIsValid(token)) {
                val decodedHeader = getDecodedHeader(token)
                // Might want to save user here to in the future...
                user = User(
                    email = decodedHeader.email,
                    role = "forklift_driver", // decodedHeader.role,
                    firstName = "", // decodedToken.firstName,
                    lastName = "", // decodedToken.lastName,
                )
                isUserAuthenticated = true
            } else {
                Log.i("EncryptionService", "onResume: token was found but is invalid")
                logout()
            }
        } else {
            Log.i("EncryptionService", "onResume: token is null or empty")
            logout()
        }
    }

    fun logout() {
        isUserAuthenticated = false
        removeEncryptedPreference("token")
    }
}
