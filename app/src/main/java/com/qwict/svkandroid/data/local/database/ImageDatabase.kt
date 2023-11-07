package com.qwict.svkandroid.data.local.database

import com.qwict.svkandroid.data.local.schema.ImageRoomEntity
import kotlinx.coroutines.flow.Flow

interface ImageDatabase {
    fun getImageFlowById(id: Int): Flow<ImageRoomEntity>
    suspend fun getImageById(id: Int): ImageRoomEntity
    suspend fun insert(image: ImageRoomEntity)
    suspend fun insertAll(images: List<ImageRoomEntity>)
    suspend fun update(image: ImageRoomEntity)
    suspend fun delete(image: ImageRoomEntity)
}
