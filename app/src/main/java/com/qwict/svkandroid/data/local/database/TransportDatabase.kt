package com.qwict.svkandroid.data.local.database

import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
import com.qwict.svkandroid.data.local.schema.TransportRoomEntityWithCargosAndImages

interface TransportDatabase {
    suspend fun getTransportByRouteNumber(routeNumber: String): TransportRoomEntity
    suspend fun insert(transport: TransportRoomEntity)
    suspend fun insertAll(transports: List<TransportRoomEntity>)
    suspend fun update(transport: TransportRoomEntity)
    suspend fun delete(transport: TransportRoomEntity)
//    suspend fun getActiveTransport(): TransportRoomEntity
    suspend fun getActiveTransport(): TransportRoomEntityWithCargosAndImages
    suspend fun getTransportsToSync(): List<TransportRoomEntityWithCargosAndImages>
}
