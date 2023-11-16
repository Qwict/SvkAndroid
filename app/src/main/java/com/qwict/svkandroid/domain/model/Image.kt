package com.qwict.svkandroid.domain.model

import java.util.UUID

data class Image(
    val imageUuid: UUID,
    val userId: Int,
    val transportId: Int,
)
