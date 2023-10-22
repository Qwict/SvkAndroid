package com.qwict.svkandroid.helper

import android.util.Log
import com.qwict.svkandroid.ui.MainViewModel

fun decodeToken(jwt: String): String {
    val (header, payload, signature) = jwt.split(".")
    return try {
        android.util.Base64.decode(header, android.util.Base64.DEFAULT).decodeToString()
        android.util.Base64.decode(payload, android.util.Base64.DEFAULT).decodeToString()
//        TODO: will have to check signature later, possibly with a public key that is added to the app...
    } catch (e: Exception) {
        "Error parsing JWT: $e"
    }
}

fun tokenIsValid(mainViewModel: MainViewModel): Boolean {
    if (mainViewModel.user.token != "") {
        val decodedToken = decodeToken(mainViewModel.user.token)
        Log.i("MainActivity", "decoded token: $decodedToken")

        val tokenExpiration = decodedToken
            .substringAfter("exp\":")
            .substringBefore(",")
//            if exp is in the end of the decoded token the before , will not work, so we need to remove the last }
            .replace("}", "")
        val currentTime = System.currentTimeMillis() / 1000
        if (tokenExpiration.toLong() < currentTime) {
            mainViewModel.user.token = "expired"
            Log.i("MainActivity", "token expired")
            return false
        }
    }
    return true
}
