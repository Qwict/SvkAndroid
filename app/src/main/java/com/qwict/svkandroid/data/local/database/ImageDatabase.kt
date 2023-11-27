package com.qwict.svkandroid.data.local.database

import com.qwict.svkandroid.data.local.schema.ImageRoomEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ImageDatabase {
    fun getImageFlowByUUID(id: String): Flow<ImageRoomEntity>
    suspend fun getImageById(id: Int): ImageRoomEntity
    suspend fun insert(image: ImageRoomEntity)
    suspend fun insertAll(images: List<ImageRoomEntity>)
    suspend fun update(image: ImageRoomEntity)
    suspend fun delete(image: ImageRoomEntity)
    suspend fun getImagesToSync(): List<ImageRoomEntity>
}
