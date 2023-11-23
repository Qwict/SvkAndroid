package com.qwict.svkandroid.data.local.schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.qwict.svkandroid.domain.model.Transport
import java.util.Date

@Entity(
    tableName = "transport",
    indices = [
        Index(
            value = arrayOf("route_number"),
            unique = true,
        ),
    ],
)
data class TransportRoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    @ColumnInfo(name = "remote_id") var remoteId: Int = 0, // could be used somewhere ... maybe add it later?
    @ColumnInfo(name = "route_number")
    val routeNumber: String,
    @ColumnInfo(name = "route_date")
    val routeDate: Date = Date(),
    val driver: String,
    @ColumnInfo(name = "license_plate")
    val licensePlate: String,
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,
    @ColumnInfo(name = "is_active_flow")
    val isActiveFlow: Boolean = true,
)

// TODO: Doesn't work?
// data class TransportRoomEntityWithCargosAndImages(
//    val transport: TransportRoomEntity,
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "id",
//    )
//    val cargos: List<CargoRoomEntity>,
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "id",
//    )
//    val images: List<ImageRoomEntity>,
// )

data class TransportRoomEntityWithCargos(
    val transport: TransportRoomEntity,
    @Relation(
        parentColumn = "route_number",
        entityColumn = "route_number",
    )
    val cargos: List<CargoRoomEntity>,
)

data class TransportRoomEntityWithImages(
    val transport: TransportRoomEntity,
    @Relation(
        parentColumn = "route_number",
        entityColumn = "route_number",
    )
    val images: List<ImageRoomEntity>,
)

data class TransportRoomEntityWithCargosAndImages(
    val transport: TransportRoomEntity,
    val transportWithCargos: TransportRoomEntityWithCargos,
    val transportWithImages: TransportRoomEntityWithImages,
)

fun TransportRoomEntity.asDomainModel() = Transport(
    routeNumber = routeNumber,
    routeDate = routeDate,
    driverName = driver,
    licensePlate = licensePlate,
)

fun TransportRoomEntityWithCargos.asDomainModel() = Transport(
    routeNumber = transport.routeNumber,
    routeDate = transport.routeDate,
    driverName = transport.driver,
    licensePlate = transport.licensePlate,
    cargos = cargos.map { it.asDomainModel() },
)

fun TransportRoomEntityWithCargosAndImages.asDomainModel() = Transport(
    routeNumber = transport.routeNumber,
    routeDate = transport.routeDate,
    driverName = transport.driver,
    licensePlate = transport.licensePlate,
    cargos = transportWithCargos.cargos.map { it.asDomainModel() },
    images = transportWithImages.images.map { it.asDomainModel() },
)

// Could insert seeds here to populate the database with some data
fun populateTransports(): List<TransportRoomEntity> {
    return listOf()
}
