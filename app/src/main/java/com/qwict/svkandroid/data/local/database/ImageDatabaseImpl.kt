package com.qwict.svkandroid.data.local.database

import com.qwict.svkandroid.data.local.dao.ImageDao
import com.qwict.svkandroid.data.local.schema.ImageRoomEntity
import java.util.UUID

class ImageDatabaseImpl(private val imageDao: ImageDao) : ImageDatabase {
    override suspend fun getImageByImageUuid(imageUuid: UUID): ImageRoomEntity {
        return imageDao.getImageByImageUuid(imageUuid)
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

    override suspend fun getImagesToSync(): List<ImageRoomEntity> {
        return imageDao.getImagesToSync()
    }
}
