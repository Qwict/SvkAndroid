package com.qwict.svkandroid.data.local.converter

import android.net.Uri
import androidx.room.TypeConverter

object UriConverter {
    @TypeConverter
    fun toUri(uri: String): Uri {
        return Uri.parse(uri)
    }

    @TypeConverter
    fun fromUri(uri: Uri): String {
        return uri.toString()
    }
}
