package com.qwict.svkandroid.data.local.database

import com.qwict.svkandroid.data.local.dao.CargoDao
import com.qwict.svkandroid.data.local.schema.CargoRoomEntity

class CargoDatabaseImpl(private val cargoDao: CargoDao) : CargoDatabase {
    override suspend fun getCargoByCargoNumber(cargoNumber: String): CargoRoomEntity {
        return cargoDao.getCargoByCargoNumber(cargoNumber)
    }

    override suspend fun getCargosByRouteNumber(routeNumber: String): List<CargoRoomEntity> {
        return cargoDao.getCargosByRouteNumber(routeNumber)
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
