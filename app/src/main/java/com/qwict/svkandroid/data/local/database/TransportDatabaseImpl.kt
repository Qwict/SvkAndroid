package com.qwict.svkandroid.data.local.database

import com.qwict.svkandroid.data.local.dao.TransportDao
import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
import kotlinx.coroutines.flow.Flow

class TransportDatabaseImpl(private val transportDao: TransportDao) : TransportDatabase {
    override fun getTransportFlowById(id: Int): Flow<TransportRoomEntity> {
        return transportDao.getTransportFlowById(id)
    }
    override suspend fun getTransportById(id: Int): TransportRoomEntity {
        return transportDao.getTransportById(id)
    }
    override suspend fun insert(transport: TransportRoomEntity) {
        transportDao.insert(transport)
    }

    override suspend fun insertAll(transports: List<TransportRoomEntity>) {
        transportDao.insertAll(transports)
    }

    override suspend fun update(transport: TransportRoomEntity) {
        transportDao.update(transport)
    }

    override suspend fun delete(transport: TransportRoomEntity) {
        transportDao.delete(transport)
    }
}