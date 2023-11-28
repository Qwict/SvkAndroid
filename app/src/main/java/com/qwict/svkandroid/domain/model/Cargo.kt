package com.qwict.svkandroid.domain.model

import com.qwict.svkandroid.data.local.schema.CargoRoomEntity

data class Cargo(
    val cargoNumber: String,
    val routeNumber: String,
    val loaderId: Int,
)

fun Cargo.asRoomEntity(): CargoRoomEntity {
    return CargoRoomEntity(
        cargoNumber = cargoNumber,
        routeNumber = routeNumber,
        loaderId = loaderId,
    )
}
