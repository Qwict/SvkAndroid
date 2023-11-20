package com.qwict.svkandroid.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageDto(
    val id: Int,
    val imageUuid: String,
    val userId: Int,
    val transportId: Int,
)
