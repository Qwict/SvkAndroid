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

@Dao
interface TransportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transport: TransportRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transports: List<TransportRoomEntity>)

    @Update
    suspend fun update(transport: TransportRoomEntity)

    @Delete
    suspend fun delete(transport: TransportRoomEntity)

    @Query("SELECT * FROM transport WHERE route_number = :routeNumber")
    fun getTransportByRouteNumber(routeNumber: String): TransportRoomEntity
    // TODO: Figure out how to get this to work


//    @Query("SELECT * FROM transport WHERE is_active = 1")
//    suspend fun getActiveTransport(): TransportRoomEntity

    @Transaction
    @Query("SELECT * FROM transport WHERE is_active = 1")
    suspend fun getActiveTransport(): TransportRoomEntityWithCargosAndImages

    @Transaction
    @Query("SELECT * FROM transport WHERE is_synced = 0")
    fun getTransportToSync(): List<TransportRoomEntityWithCargosAndImages>
}
