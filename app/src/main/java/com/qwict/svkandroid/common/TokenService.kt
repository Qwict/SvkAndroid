package com.qwict.svkandroid.common

import android.util.Base64
import android.util.Log
import com.qwict.svkandroid.data.saveEncryptedPreference

fun decodeToken(jwt: String): String {
    val (header, payload, signature) = jwt.split(".")
//    TODO: Might want to implement signature check in the future

    return try {
        Base64.decode(header, Base64.DEFAULT).decodeToString()
        Base64.decode(payload, Base64.DEFAULT).decodeToString()
    } catch (e: Exception) {
        "Error parsing JWT: $e"
    }
}

fun tokenIsValid(token: String): Boolean {
    if (token != "") {
        val decodedToken = decodeToken(token)
        Log.i("MainActivity", "decoded token: $decodedToken")

        val tokenExpiration = decodedToken
            .substringAfter("exp\":")
            .substringBefore(",")
//            if exp is in the end of the decoded token the before , will not work, so we need to remove the last }
            .replace("}", "")
        val currentTime = System.currentTimeMillis() / 1000
        if (tokenExpiration.toLong() > currentTime) {
            Log.i("MainActivity", "token still valid")
            return true
        }
    }
    return false
    saveEncryptedPreference("token", "expired")
}
