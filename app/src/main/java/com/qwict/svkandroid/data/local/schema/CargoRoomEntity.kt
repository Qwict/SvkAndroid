package com.qwict.svkandroid.data.local.schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qwict.svkandroid.data.remote.dto.CargoDto
import com.qwict.svkandroid.domain.model.Cargo

@Entity(tableName = "cargo")
data class CargoRoomEntity(
    @ColumnInfo(name = "cargo_number")
    @PrimaryKey
    val cargoNumber: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "route_number")
    val routeNumber: String,
    @ColumnInfo(name = "loader_id")
    val loaderId: Int,
)

fun CargoRoomEntity.asDomainModel() = Cargo(
    cargoNumber = cargoNumber,
    loaderId = loaderId,
    routeNumber = routeNumber,
)

fun CargoRoomEntity.asCargoDto() = CargoDto(
    cargoNumber = cargoNumber,
    createdAt = createdAt,
    loaderId = loaderId,
    routeNumber = routeNumber,
)
