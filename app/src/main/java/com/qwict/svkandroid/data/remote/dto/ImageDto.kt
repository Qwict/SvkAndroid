package com.qwict.svkandroid.data.remote.dto

import android.net.Uri
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageDto(
    val imageUuid: String,
    val localUri: Uri,
    val createdAt: Long,
    val routeNumber: String,
    val loaderId: Int,
)
