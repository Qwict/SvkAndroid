package com.qwict.svkandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.qwict.svkandroid.data.local.schema.ImageRoomEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: ImageRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ImageRoomEntity>)

    @Update
    suspend fun update(image: ImageRoomEntity)

    @Delete
    suspend fun delete(image: ImageRoomEntity)

    @Query("SELECT * FROM image WHERE image_uuid = :id")
    fun getImageFlowById(id: String): Flow<ImageRoomEntity>

    @Query("SELECT * FROM image WHERE image_uuid = :imageUuid")
    fun getImageByImageUuid(imageUuid: UUID): ImageRoomEntity

    @Query("SELECT * FROM image WHERE is_synced = 0")
    fun getImagesToSync(): List<ImageRoomEntity>
}
