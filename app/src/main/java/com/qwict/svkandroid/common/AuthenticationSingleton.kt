package com.qwict.svkandroid.common

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.qwict.svkandroid.common.Constants.EMPTY_USER
import com.qwict.svkandroid.data.local.getEncryptedPreference
import com.qwict.svkandroid.data.local.removeEncryptedPreference
import com.qwict.svkandroid.domain.model.User

/**
 * Singleton class responsible for managing user authentication state.
 */
object AuthenticationSingleton {
    /**
     * Mutable state representing whether the user is authenticated or not.
     */
    var isUserAuthenticated by mutableStateOf(false)
        private set

    /**
     * Mutable state representing the user information.
     */
    var user by mutableStateOf(EMPTY_USER)
        private set

    /**
     * Validates the user by checking the token stored in the encrypted preferences.
     * If the token is valid, it sets [isUserAuthenticated] to true and populates the [user] property.
     * Otherwise, it logs out the user.
     */
    fun validateUser() {
        val token = getEncryptedPreference("token")
        if (token != null && token != "") {
            if (tokenIsValid(token)) {
                val decodedHeader = getDecodedPayload(token)
                // Might want to save user here to in the future...
                user = User(
                    email = decodedHeader.email,
                    role = decodedHeader.role,
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

    /**
     * Logs out the user by setting [isUserAuthenticated] to false and removing the token from encrypted preferences.
     */
    fun logout() {
        isUserAuthenticated = false
        removeEncryptedPreference("token")
    }
}
