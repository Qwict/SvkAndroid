package com.qwict.svkandroid.domain.model

import com.qwict.svkandroid.data.local.schema.ImageRoomEntity

data class Image(
    val imageUuid: String
)

fun Image.asRoomEntity(): ImageRoomEntity {
    return ImageRoomEntity(
        imageUuid = imageUuid
    )
}