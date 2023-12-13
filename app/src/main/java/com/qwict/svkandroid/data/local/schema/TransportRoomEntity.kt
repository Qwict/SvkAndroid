package com.qwict.svkandroid.data.local.schema

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.qwict.svkandroid.data.remote.dto.TransportDto
import com.qwict.svkandroid.domain.model.Transport

@Entity(tableName = "transport")
data class TransportRoomEntity(
    @PrimaryKey
    @ColumnInfo(name = "route_number")
    val routeNumber: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "license_plate")
    val licensePlate: String,
    @ColumnInfo(name = "driver_name")
    val driverName: String,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,
)

data class TransportRoomEntityWithCargosAndImages(
    @Embedded
    val transport: TransportRoomEntity,
    @Relation(
        parentColumn = "route_number",
        entityColumn = "route_number",
        entity = CargoRoomEntity::class
    )
    val cargos: List<CargoRoomEntity>,
    @Relation(
        parentColumn = "route_number",
        entityColumn = "route_number",
        entity = ImageRoomEntity::class
    )
    val images: List<ImageRoomEntity>,
)

fun TransportRoomEntity.asDomainModel() = Transport(
    routeNumber = routeNumber,
    driverName = driverName,
    licensePlate = licensePlate,
)

fun TransportRoomEntityWithCargosAndImages.asDomainModel() = Transport(
    routeNumber = transport.routeNumber,
    driverName = transport.driverName,
    licensePlate = transport.licensePlate,
    images = images.map { it.asDomainModel() },
    cargos = cargos.map { it.asDomainModel() }

)

fun TransportRoomEntityWithCargosAndImages.asTransportDto() = TransportDto(
    routeNumber = transport.routeNumber,
    createdAt = transport.createdAt,
    driverName = transport.driverName,
    licensePlate = transport.licensePlate,
    cargos = cargos.map { it.asCargoDto() },
    images = images.map { it.asImageDto() },
)
