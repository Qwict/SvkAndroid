package com.qwict.svkandroid.common

import android.util.Log
import com.auth0.android.jwt.JWT
import com.qwict.svkandroid.data.local.removeEncryptedPreference

/**
 * Decodes the payload of a JWT (JSON Web Token) and returns a [DecodedPayload] object.
 *
 * @param token The JWT to decode.
 * @return The decoded payload as a [DecodedPayload] object.
 * @throws IllegalArgumentException If the JWT is not valid or contains missing claims.
 */
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

/**
 * Checks whether a given JWT is valid based on its expiration time.
 *
 * @param token The JWT to validate.
 * @return `true` if the JWT is valid; `false` otherwise.
 */
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
}
