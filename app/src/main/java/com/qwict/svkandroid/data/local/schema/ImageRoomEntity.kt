package com.qwict.svkandroid.data.local.schema
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.qwict.svkandroid.data.remote.dto.ImageDto
import com.qwict.svkandroid.domain.model.Image
import java.util.UUID

@Entity(tableName = "image", foreignKeys = [
    ForeignKey(
        entity = TransportRoomEntity::class,
        parentColumns = ["route_number"],
        childColumns = ["route_number"],
        onDelete = ForeignKey.CASCADE
    )
])
data class ImageRoomEntity(
    @PrimaryKey
    @ColumnInfo(name = "image_uuid")
    val imageUuid: UUID,
    @ColumnInfo(name = "local_uri")
    val localUri: Uri,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "route_number")
    val routeNumber: String,
    @ColumnInfo(name = "loader_id")
    val loaderId: Int,
    @ColumnInfo(name = "is_synced")
    val isSynced: Boolean = false,
)

fun ImageRoomEntity.asImageDto(): ImageDto {
    return ImageDto(
        imageUuid = imageUuid.toString(),
//        localUri = localUri,
        createdAt = createdAt,
        loaderId = loaderId,
        routeNumber = routeNumber,
    )
}

fun ImageRoomEntity.asDomainModel() = Image(
    imageUuid = imageUuid,
    localUri = localUri,
    loaderId = loaderId,
    routeNumber = routeNumber
)