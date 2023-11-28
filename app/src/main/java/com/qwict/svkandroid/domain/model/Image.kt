package com.qwict.svkandroid.domain.model

import android.net.Uri
import com.qwict.svkandroid.data.local.schema.ImageRoomEntity
import java.util.UUID

data class Image(
    val imageUuid: UUID,
    val localUri: Uri,
    val loaderId: Int,
    val routeNumber: String,
)

fun Image.asRoomEntity(): ImageRoomEntity {
    return ImageRoomEntity(
        imageUuid = imageUuid,
        localUri = localUri,
        loaderId = loaderId,
        routeNumber = routeNumber,
    )
}
