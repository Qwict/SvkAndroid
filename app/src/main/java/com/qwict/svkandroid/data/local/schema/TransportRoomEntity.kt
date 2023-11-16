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
    val routeNumber: Int = 0,
    @ColumnInfo(name = "route_date")
    val routeDate: Date = Date(),
    val driver: String = "",
    @ColumnInfo(name = "license_plate")
    val licensePlate: String = "",
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
        parentColumn = "id",
        entityColumn = "id",
    )
    val cargos: List<CargoRoomEntity>,
)

data class TransportRoomEntityWithImages(
    val transport: TransportRoomEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val images: List<ImageRoomEntity>,
)

fun TransportRoomEntity.toTransport() = Transport(
    routeNumber = routeNumber,
    routeDate = routeDate,
    driver = driver,
    licensePlate = licensePlate,
)

// Could insert seeds here to populate the database with some data
fun populateTransports(): List<TransportRoomEntity> {
    return listOf()
}
