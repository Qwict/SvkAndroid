package com.qwict.svkandroid.data.local.database

import android.util.Log
import com.qwict.svkandroid.data.local.dao.TransportDao
import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
import com.qwict.svkandroid.data.local.schema.TransportRoomEntityWithCargosAndImages

class TransportDatabaseImpl(private val transportDao: TransportDao) : TransportDatabase {
    override suspend fun getTransportByRouteNumber(routeNumber: String): TransportRoomEntity {
        Log.d("TransportDatabase", "Getting Transport object $routeNumber")
        return transportDao.getTransportByRouteNumber(routeNumber)
    }

    override suspend fun insert(transport: TransportRoomEntity) {
        Log.d("TransportDatabase", "Inserted Transport object ${transport.routeNumber}")
        transportDao.insert(transport)
    }

    override suspend fun insertAll(transports: List<TransportRoomEntity>) {
        transportDao.insertAll(transports)
    }

    override suspend fun update(transport: TransportRoomEntity) {
        Log.d("TransportDatabase", "Updated Transport object ${transport.routeNumber}")
        transportDao.update(transport)
    }

    override suspend fun delete(transport: TransportRoomEntity) {
        Log.d("TransportDatabase", "Deleted Transport object ${transport.routeNumber}")
        transportDao.delete(transport)
    }

    override suspend fun getActiveTransport(): TransportRoomEntity {
        return transportDao.getActiveTransport()
    }

    override suspend fun getTransportsToSync(): List<TransportRoomEntityWithCargosAndImages> {
        return transportDao.getTransportToSync()
    }
}
