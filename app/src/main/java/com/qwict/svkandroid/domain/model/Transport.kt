package com.qwict.svkandroid.domain.model

import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
import com.qwict.svkandroid.data.local.schema.UserRoomEntity
import java.util.Date
import java.util.UUID

data class Transport(
    val routeNumber: Int = 0,
    val routeDate: Date = Date(),
    val driver: String = "",
    val licensePlate: String = "",
    val imageUuids: List<UUID> = emptyList(),
    val loaderId: Int = 0,
    val cargos: List<Cargo> = emptyList(),
)

fun Transport.asRoomEntity(): TransportRoomEntity {
    return TransportRoomEntity(
        routeNumber = routeNumber,
        routeDate = routeDate
    )
}

