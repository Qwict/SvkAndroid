package com.qwict.svkandroid.data.local.schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.qwict.svkandroid.domain.model.Cargo
import java.util.Date

@Entity(
    tableName = "cargo",
    indices = [
        Index(
            value = arrayOf("cargo_number"),
            unique = true,
        ),
    ],
)
data class CargoRoomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
//    @ColumnInfo(name = "remote_id") var remoteId: Int = 0, // could be used somewhere ... maybe add it later?
    @ColumnInfo(name = "cargo_number")
    val cargoNumber: String = "",
    @ColumnInfo(name = "cargo_date")
    val cargoDate: Date = Date(),

//    TODO: Ask; Might be doing foreign keys wrong here... (slave end of the relationship (other side will be fine...)
    @ColumnInfo(name = "route_number")
    val routeNumber: String = "",
    @ColumnInfo(name = "loader_id")
    val loaderId: Int = 0,

)

fun CargoRoomEntity.asDomainModel() = Cargo(
    cargoNumber = cargoNumber,
    loaderId = loaderId,
)

// Could insert seeds here to populate the database with some data
fun populateCargos(): List<CargoRoomEntity> {
    return listOf()
}
