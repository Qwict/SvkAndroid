package com.qwict.svkandroid.data.remote.dto

import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class CargoDto(
    val id: Int,
    val cargoNumber: String,
    val cargoDate: Date,
    val transportId: Int,
)
