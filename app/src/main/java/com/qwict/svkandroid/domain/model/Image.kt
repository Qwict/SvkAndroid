package com.qwict.svkandroid.domain.model

import com.qwict.svkandroid.data.local.schema.ImageRoomEntity
import java.util.UUID

data class Image(
    val imageUuid: UUID,
    val loaderId: Int,
    val routeNumber: String,
)

fun Image.asRoomEntity(): ImageRoomEntity {
    return ImageRoomEntity(
        imageUuid = imageUuid,
        loaderId = loaderId,
        routeNumber = routeNumber,
    )
}
