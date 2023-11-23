package com.qwict.svkandroid.data.local.database

import com.qwict.svkandroid.data.local.dao.CargoDao
import com.qwict.svkandroid.data.local.schema.CargoRoomEntity
import kotlinx.coroutines.flow.Flow

class CargoDatabaseImpl(private val cargoDao: CargoDao) : CargoDatabase {
    override fun getCargoFlowByCargoNumber(cargoNumber: String): Flow<CargoRoomEntity> {
        return cargoDao.getCargoFlowByCargoNumber(cargoNumber)
    }
    override suspend fun getCargoById(id: Int): CargoRoomEntity {
        return cargoDao.getCargoByCargoNumber(id)
    }

    override suspend fun getCargosByTransportId(transportId: Int): List<CargoRoomEntity> {
        return cargoDao.getCargosByTransportId(transportId)
    }

    override suspend fun insert(cargo: CargoRoomEntity) {
        cargoDao.insert(cargo)
    }

    override suspend fun insertAll(cargos: List<CargoRoomEntity>) {
        cargoDao.insertAll(cargos)
    }

    override suspend fun update(cargo: CargoRoomEntity) {
        cargoDao.update(cargo)
    }

    override suspend fun delete(cargo: CargoRoomEntity) {
        cargoDao.delete(cargo)
    }
}
