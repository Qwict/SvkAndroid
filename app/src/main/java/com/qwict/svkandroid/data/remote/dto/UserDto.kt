package com.qwict.svkandroid.data.remote.dto

import com.qwict.svkandroid.data.local.schema.UserRoomEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: Int = 0,
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
)

fun UserDto.asRoomEntity(): UserRoomEntity {
    return UserRoomEntity(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
    )
}
