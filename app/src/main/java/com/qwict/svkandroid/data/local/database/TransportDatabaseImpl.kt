package com.qwict.svkandroid.data.local.database

import android.util.Log
import com.qwict.svkandroid.data.local.dao.TransportDao
import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
import kotlinx.coroutines.flow.Flow

class TransportDatabaseImpl(private val transportDao: TransportDao) : TransportDatabase {
    override fun getTransportFlowById(id: Int): Flow<TransportRoomEntity> {
        return transportDao.getTransportFlowById(id)
    }
    override suspend fun getTransportById(id: Int): TransportRoomEntity {
        Log.d("TransportDatabase", "Got transport by id $id")
        return transportDao.getTransportById(id)
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
}
