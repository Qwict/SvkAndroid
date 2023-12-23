package com.qwict.svkandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
import com.qwict.svkandroid.data.local.schema.TransportRoomEntityWithCargosAndImages

/**
 * The Data Access Object for the TransportRoomEntity class.
 */
@Dao
interface TransportDao {
    /**
     * Inserts a single transport into the local database. If a conflict occurs, the existing transport is replaced.
     *
     * @param transport The transport to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transport: TransportRoomEntity)

    /**
     * Inserts a list of transports into the local database. If a conflict occurs, the existing transports are replaced.
     *
     * @param transports The list of transports to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transports: List<TransportRoomEntity>)

    /**
     * Updates a transport in the local database.
     *
     * @param transport The transport to be updated.
     */
    @Update
    suspend fun update(transport: TransportRoomEntity)

    /**
     * Deletes a transport from the local database.
     *
     * @param transport The transport to be deleted.
     */
    @Delete
    suspend fun delete(transport: TransportRoomEntity)

    /**
     * Retrieves a transport from the local database by route number.
     *
     * @param routeNumber The route number of the transport to retrieve.
     * @return The transport with the specified route number.
     */
    @Query("SELECT * FROM transport WHERE route_number = :routeNumber")
    fun getTransportByRouteNumber(routeNumber: String): TransportRoomEntity

    /**
     * Retrieves the active transport from the local database. The result includes cargos and images associated with the transport.
     *
     * @return The active transport with associated cargos and images.
     */
    @Transaction
    @Query("SELECT * FROM transport WHERE is_active = 1")
    suspend fun getActiveTransport(): TransportRoomEntityWithCargosAndImages

    /**
     * Retrieves a list of transports from the local database that are marked for synchronization. The result includes cargos and images associated with each transport.
     *
     * @return A list of transports to be synchronized, each with associated cargos and images.
     */
    @Transaction
    @Query("SELECT * FROM transport WHERE is_synced = 0 AND is_active = 0")
    fun getTransportToSync(): List<TransportRoomEntityWithCargosAndImages>
}
