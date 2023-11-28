package com.qwict.svkandroid.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransportDto(
    val routeNumber: String,
    val createdAt: Long,
    val driverName: String,
    val licensePlate: String,
    val cargos: List<CargoDto>,
    val images: List<ImageDto>,
)
