package com.qwict.svkandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM transport WHERE id = :id")
    fun getTransportFlowById(id: Int): Flow<TransportRoomEntity>

    @Query("SELECT * FROM transport WHERE id = :id")
    fun getTransportById(id: Int): TransportRoomEntity
    // TODO: Figure out how to get this to work

    @Query("SELECT * FROM transport WHERE is_active_flow = 1")
    suspend fun getActiveTransport(): TransportRoomEntity

    @Query("SELECT * FROM transport WHERE is_synced = 0")
    suspend fun getTransportsToSync(): List<TransportRoomEntity>
//    @Transaction
//    @Query("SELECT * FROM transport")
//    fun getTransportWithCargosAndImages(): List<TransportRoomEntityWithCargosAndImages>
}
