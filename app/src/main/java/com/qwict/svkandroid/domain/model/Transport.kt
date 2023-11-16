package com.qwict.svkandroid.domain.model

import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
import com.qwict.svkandroid.data.local.schema.TransportRoomEntityWithCargos
import java.util.Date

data class Transport(
    val routeNumber: String = "",
    val routeDate: Date = Date(),
    val driverName: String = "",
    val licensePlate: String = "",
    val loaderId: Int = 0,
    val images: List<Image> = emptyList(),
    val cargos: List<Cargo> = emptyList(),
)

fun Transport.asRoomEntity(): TransportRoomEntity {
    return TransportRoomEntity(
        routeNumber = routeNumber,
        routeDate = routeDate,
        driver = driverName,
        licensePlate = licensePlate,
    )
}

fun Transport.asRoomEntityWithCargos(): TransportRoomEntityWithCargos {
    return TransportRoomEntityWithCargos(
        transport = asRoomEntity(),
        cargos = cargos.map { it.asRoomEntity() },
    )
}
