package com.qwict.svkandroid.dto

import android.graphics.Picture
import android.util.Log
import com.auth0.android.jwt.JWT

data class User(val jwt: String? = null) {

    private val TAG = "User"

    var id = 0
    var email = ""
    var token = ""

//    I suggest we save things from the user here, user is available in viewModel.user
    var pictures: MutableList<Picture> = mutableListOf<Picture>()

    init {
        if (jwt != null) {
            try {
                val jwt = JWT(jwt ?: "")
                id = jwt.getClaim("id").asInt()!!
                email = jwt.getClaim("email").asString() ?: ""
                token = this.jwt
            } catch (error: com.auth0.android.jwt.DecodeException) {
                Log.e(TAG, "Error occurred trying to decode JWT: $error ")
            }
        } else {
            Log.d(TAG, "User is logged out - instantiating empty User object.")
        }
    }
}
