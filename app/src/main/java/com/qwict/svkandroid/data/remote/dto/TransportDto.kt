package com.qwict.svkandroid.data.remote.dto

import com.qwict.svkandroid.data.local.schema.UserRoomEntity
import com.squareup.moshi.JsonClass
import java.util.Date
import java.util.UUID

@JsonClass(generateAdapter = true)
data class TransportDto(
    val routeNumber: String,
    val routeDate: Date,
    val cargoDate: Date,
    val driver: String,
    val licensePlate: String,
    val imagesBlobUuid: UUID,
)
