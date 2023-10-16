package com.qwict.svkandroid.helper

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64

// Feeling good, might use later
@RequiresApi(Build.VERSION_CODES.O)
fun decodeToken(jwt: String): String {
    val parts = jwt.split(".")
    return try {
        val charset = charset("UTF-8")
        val header = String(Base64.getUrlDecoder().decode(parts[0].toByteArray(charset)), charset)
        val payload = String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
        "$header"
        "$payload"
    } catch (e: Exception) {
        "Error parsing JWT: $e"
    }
}
