package com.qwict.svkandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.qwict.svkandroid.data.local.schema.CargoRoomEntity

/**
 * The Data Access Object for the CargoRoomEntity class.
 */
@Dao
interface CargoDao {
    /**
     * Inserts a single cargo into the local database. If a conflict occurs, the existing cargo is replaced.
     *
     * @param cargo The cargo to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cargo: CargoRoomEntity)

    /**
     * Inserts a list of cargos into the local database. If a conflict occurs, the existing cargos are replaced.
     *
     * @param cargos The list of cargos to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cargos: List<CargoRoomEntity>)

    /**
     * Updates a cargo in the local database.
     *
     * @param cargo The cargo to be updated.
     */
    @Update
    suspend fun update(cargo: CargoRoomEntity)

    /**
     * Deletes a cargo from the local database.
     *
     * @param cargo The cargo to be deleted.
     */
    @Delete
    suspend fun delete(cargo: CargoRoomEntity)

    /**
     * Retrieves a cargo from the local database by cargo number.
     *
     * @param cargoNumber The cargo number of the cargo to retrieve.
     * @return The cargo with the specified cargo number.
     */
    @Query("SELECT * FROM cargo WHERE cargo_number LIKE :cargoNumber")
    fun getCargoByCargoNumber(cargoNumber: String): CargoRoomEntity

    /**
     * Retrieves all cargos from the local database for a specified route number.
     *
     * @param routeNumber The route number for which to retrieve cargos.
     * @return A list of cargos for the specified route number.
     */
    @Query("SELECT * FROM cargo WHERE route_number = :routeNumber")
    suspend fun getCargosByRouteNumber(routeNumber: String): List<CargoRoomEntity>
}
