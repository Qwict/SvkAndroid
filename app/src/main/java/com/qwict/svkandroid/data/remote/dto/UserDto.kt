package com.qwict.svkandroid.data.remote.dto

import com.qwict.svkandroid.data.local.schema.UserRoomEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    val userId: Long = 0,
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val role: String = "",
    val token: String = "",
    val validated: Boolean = false,
)

fun UserDto.asRoomEntity(): UserRoomEntity {
    return UserRoomEntity(
        email = email,
        firstName = firstName,
        lastName = lastName,
        remoteId = userId,
    )
}
