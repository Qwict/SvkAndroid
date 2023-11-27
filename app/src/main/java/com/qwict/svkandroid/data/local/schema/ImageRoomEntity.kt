package com.qwict.svkandroid.data.local.schema
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.qwict.svkandroid.domain.model.Image
import java.util.UUID

@Entity(
    tableName = "image",
    indices = [
        Index(
            value = arrayOf("image_uuid"),
            unique = true,
        ),
    ],
)
data class ImageRoomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // The image UUID that will be used in the blob storage (file name)

    @ColumnInfo(name = "image_uuid")
    val imageUuid: String = UUID.randomUUID().toString(),

    // The user (loader) that took the picture
    @ColumnInfo(name = "user_id")
    val userId: Int = 0,

    // The transport that the picture was taken for
    @ColumnInfo(name = "transport_id")
    val transportId: Int = 0,

    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,
    @ColumnInfo(name = "route_number")
    val routeNumber: String = ""
)

fun ImageRoomEntity.asDomainModel(): Image {
    return Image(
        imageUuid = imageUuid
    )
}

// Could insert seeds here to populate the database with some data
fun populateImages(): List<ImageRoomEntity> {
    return listOf()
}
