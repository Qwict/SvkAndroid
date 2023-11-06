package com.qwict.svkandroid.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthenticatedUserDto(
    val token: String,
    val user: UserDto,
    val validated: Boolean,
)

// TODO: Ask: Should not exist because Flow is always Api -> Database -> UI (so Api -> UI is not possible)
// fun AuthenticatedUserDto.toUser() = User(
//    email = user.email,
//    id = user.id,
//    name = user.name,
// )
