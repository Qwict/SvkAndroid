package com.qwict.svkandroid.domain.model

import java.util.Date
import java.util.UUID

data class Transport(
    val routeNumber: Int = 0,
    val routeDate: Date = Date(),
    val cargoDate: Date = Date(),
    val driver: String = "",
    val licensePlate: String = "",
    val imageUuids: List<UUID> = emptyList(),
    val loaderId: Int = 0,
    val cargos: List<Cargo> = emptyList(),
)
