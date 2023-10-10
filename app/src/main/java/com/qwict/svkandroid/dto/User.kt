package com.qwict.svkandroid.dto

import android.util.Log
import com.auth0.android.jwt.JWT


data class User(val idToken: String? = null) {

    private val TAG = "User"

    var id = ""
    var name = ""
    var email = ""
    var emailVerified = ""
    var picture = ""
    var updatedAt = ""

    // Make choice between firebase and doind Authentication ourself
    init {
        if (idToken != null) {
            try {
                // Attempt to decode the ID token.
                val jwt = JWT(idToken ?: "")

                // The ID token is a valid JWT,
                // so extract information about the user from it.
                id = jwt.subject ?: ""
                name = jwt.getClaim("name").asString() ?: ""
                email = jwt.getClaim("email").asString() ?: ""
                emailVerified = jwt.getClaim("email_verified").asString() ?: ""
                picture = jwt.getClaim("picture").asString() ?: ""
                updatedAt = jwt.getClaim("updated_at").asString() ?: ""
            } catch (error: com.auth0.android.jwt.DecodeException) {
                // The ID token is NOT a valid JWT, so log the error
                // and leave the user properties as empty strings.
                Log.e(TAG, "Error occurred trying to decode JWT: ${error.toString()} ")
            }
        } else {
            // The User object was instantiated with a null value,
            // which means the user is being logged out.
            // The user properties will be set to empty strings.
            Log.d(TAG, "User is logged out - instantiating empty User object.")
        }
    }

}