package com.qwict.svkandroid.data.local.database

import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
import kotlinx.coroutines.flow.Flow

interface TransportDatabase {
    fun getTransportFlowById(id: Int): Flow<TransportRoomEntity>
    suspend fun getTransportById(id: Int): TransportRoomEntity
    suspend fun insert(transport: TransportRoomEntity)
    suspend fun insertAll(transports: List<TransportRoomEntity>)
    suspend fun update(transport: TransportRoomEntity)
    suspend fun delete(transport: TransportRoomEntity)
    suspend fun getActiveTransport(): TransportRoomEntity
    suspend fun getTransportsToSync(): List<TransportRoomEntity>
}
