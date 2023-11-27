package com.qwict.svkandroid.data.local.database

import com.qwict.svkandroid.data.local.schema.CargoRoomEntity
import kotlinx.coroutines.flow.Flow

interface CargoDatabase {
    fun getCargoFlowByCargoNumber(cargoNumber: String): Flow<CargoRoomEntity>
    suspend fun getCargoById(id: Int): CargoRoomEntity
    suspend fun getCargosByTransportId(transportId: Int): List<CargoRoomEntity>
    suspend fun insert(cargo: CargoRoomEntity)
    suspend fun insertAll(cargos: List<CargoRoomEntity>)
    suspend fun update(cargo: CargoRoomEntity)
    suspend fun delete(cargo: CargoRoomEntity)
    suspend fun getCargosToSync(): List<CargoRoomEntity>
}
