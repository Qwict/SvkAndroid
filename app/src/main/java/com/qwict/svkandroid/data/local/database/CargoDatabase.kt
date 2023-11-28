package com.qwict.svkandroid.data.local.database

import com.qwict.svkandroid.data.local.schema.CargoRoomEntity

interface CargoDatabase {
    suspend fun getCargoByCargoNumber(cargoNumber: String): CargoRoomEntity
    suspend fun getCargosByRouteNumber(routeNumber: String): List<CargoRoomEntity>
    suspend fun insert(cargo: CargoRoomEntity)
    suspend fun insertAll(cargos: List<CargoRoomEntity>)
    suspend fun update(cargo: CargoRoomEntity)
    suspend fun delete(cargo: CargoRoomEntity)
}
