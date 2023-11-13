package com.qwict.svkandroid.common

import android.util.Log
import com.auth0.android.jwt.JWT
import com.qwict.svkandroid.data.local.removeEncryptedPreference
import com.qwict.svkandroid.data.local.saveEncryptedPreference

fun getDecodedHeader(token: String): DecodedHeader {
    val jwt = JWT(token)
    try {
        return DecodedHeader(
            alg = jwt.getClaim("alg").asString()!!,
            typ = jwt.getClaim("typ").asString()!!,
        )
    } catch (e: NullPointerException) {
        throw IllegalArgumentException("JWT is not valid")
    }
}

fun getDecodedPayload(token: String): DecodedPayload {
    val jwt = JWT(token)
    try {
        return DecodedPayload(
            email = jwt.getClaim("email").asString()!!,
            role = jwt.getClaim("role").asString()!!,
            userId = jwt.getClaim("userId").asInt()!!,
            iat = jwt.getClaim("iat").asInt()!!,
            exp = jwt.getClaim("exp").asInt()!!,
        )
    } catch (e: NullPointerException) {
        removeEncryptedPreference("token")
        throw IllegalArgumentException("JWT is not valid")
    }
}

fun tokenIsValid(token: String): Boolean {
    if (token != "") {
        val decodedToken = getDecodedPayload(token)
        Log.i("MainActivity", "decoded token: $decodedToken")
        val tokenExpiration = decodedToken.exp
        val currentTime = System.currentTimeMillis() / 1000
        if (tokenExpiration.toLong() > currentTime) {
            Log.i("MainActivity", "token still valid")
            return true
        }
    }
    return false
    saveEncryptedPreference("token", "expired")
}
