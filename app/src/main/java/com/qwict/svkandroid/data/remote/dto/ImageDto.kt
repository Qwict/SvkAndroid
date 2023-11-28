package com.qwict.svkandroid.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageDto(
    val imageUuid: String,
    val createdAt: Long,
    val routeNumber: String,
    val loaderId: Int,
)
