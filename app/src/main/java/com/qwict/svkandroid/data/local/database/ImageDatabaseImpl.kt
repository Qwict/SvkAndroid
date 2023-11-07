package com.qwict.svkandroid.data.local.database

import com.qwict.svkandroid.data.local.dao.ImageDao
import com.qwict.svkandroid.data.local.schema.ImageRoomEntity
import kotlinx.coroutines.flow.Flow

class ImageDatabaseImpl(private val imageDao: ImageDao) : ImageDatabase {
    override fun getImageFlowById(id: Int): Flow<ImageRoomEntity> {
        return imageDao.getImageFlowById(id)
    }

    override suspend fun getImageById(id: Int): ImageRoomEntity {
        return imageDao.getImageById(id)
    }

    override suspend fun insert(image: ImageRoomEntity) {
        imageDao.insert(image)
    }

    override suspend fun insertAll(images: List<ImageRoomEntity>) {
        imageDao.insertAll(images)
    }

    override suspend fun update(image: ImageRoomEntity) {
        imageDao.update(image)
    }

    override suspend fun delete(image: ImageRoomEntity) {
        imageDao.delete(image)
    }
}
