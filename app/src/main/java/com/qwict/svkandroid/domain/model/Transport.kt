package com.qwict.svkandroid.domain.model

import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
import com.qwict.svkandroid.data.local.schema.TransportRoomEntityWithCargosAndImages

data class Transport(
    val routeNumber: String,
    val driverName: String = "",
    val licensePlate: String = "",
    val images: List<Image> = emptyList(),
    val cargos: List<Cargo> = emptyList(),
)

fun Transport.asRoomEntity(): TransportRoomEntity {
    return TransportRoomEntity(
        routeNumber = routeNumber,
        licensePlate = licensePlate,
        driverName = driverName,
    )
}

// TODO: We might want to start using this to simplify repository implementation...
fun Transport.asCompleteRoomEntity(): TransportRoomEntityWithCargosAndImages {
    return TransportRoomEntityWithCargosAndImages(
        transport = asRoomEntity(),
        images = images.map { it.asRoomEntity() },
        cargos = cargos.map { it.asRoomEntity() },
    )
}
