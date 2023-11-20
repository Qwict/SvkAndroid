package com.qwict.svkandroid.data.remote.dto

import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class TransportDto(
    val id: Int,
    val routeNumber: String,
    val routeDate: Date,
    val cargoDate: Date,
    val driver: String,
    val licensePlate: String,
)
