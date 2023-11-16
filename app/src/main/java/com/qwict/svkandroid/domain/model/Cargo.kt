package com.qwict.svkandroid.domain.model

import com.qwict.svkandroid.data.local.schema.CargoRoomEntity

data class Cargo(
    val cargoNumber: String = "",
    val loaderId: Int = 0,
)

fun Cargo.asRoomEntity(): CargoRoomEntity {
    return CargoRoomEntity(
        cargoNumber = cargoNumber,
        loaderId = loaderId,
    )
}
