package com.qwict.svkandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.qwict.svkandroid.data.local.schema.ImageRoomEntity
import java.util.UUID

/**
 * The Data Access Object for the ImageRoomEntity class.
 */
@Dao
interface ImageDao {
    /**
     * Inserts a single image into the local database. If a conflict occurs, the existing image is replaced.
     *
     * @param image The image to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: ImageRoomEntity)

    /**
     * Inserts a list of images into the local database. If a conflict occurs, the existing images are replaced.
     *
     * @param images The list of images to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ImageRoomEntity>)

    /**
     * Updates an image in the local database.
     *
     * @param image The image to be updated.
     */
    @Update
    suspend fun update(image: ImageRoomEntity)

    /**
     * Deletes an image from the local database.
     *
     * @param image The image to be deleted.
     */
    @Delete
    suspend fun delete(image: ImageRoomEntity)

    /**
     * Retrieves an image from the local database by image UUID.
     *
     * @param imageUuid The UUID of the image to retrieve.
     * @return The image with the specified image UUID.
     */
    @Query("SELECT * FROM image WHERE image_uuid = :imageUuid")
    fun getImageByImageUuid(imageUuid: UUID): ImageRoomEntity

    /**
     * Retrieves a list of images from the local database that are marked for synchronization.
     *
     * @return A list of images to be synchronized.
     */
    @Query("SELECT * FROM image WHERE is_synced = 0")
    fun getImagesToSync(): List<ImageRoomEntity>

    /**
     * Retrieves a list of images from the local database for a specified route number.
     *
     * @param routeNumber The route number for which to retrieve images.
     * @return A list of images for the specified route number.
     */
    @Query("SELECT * FROM image WHERE route_number = :routeNumber")
    fun getImagesByRouteNumber(routeNumber: String): List<ImageRoomEntity>
}
