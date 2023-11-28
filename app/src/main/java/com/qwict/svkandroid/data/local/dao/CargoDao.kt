package com.qwict.svkandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.qwict.svkandroid.data.local.schema.CargoRoomEntity

@Dao
interface CargoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cargo: CargoRoomEntity)

//    TODO: Replace might not be good...
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cargos: List<CargoRoomEntity>)

    @Update
    suspend fun update(cargo: CargoRoomEntity)

    @Delete
    suspend fun delete(cargo: CargoRoomEntity)

    @Query("SELECT * FROM cargo WHERE cargo_number LIKE :cargoNumber")
    fun getCargoByCargoNumber(cargoNumber: String): CargoRoomEntity

    @Query("SELECT * FROM cargo WHERE route_number = :routeNumber")
    suspend fun getCargosByRouteNumber(routeNumber: String): List<CargoRoomEntity>
}
