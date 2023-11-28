package com.qwict.svkandroid.data.remote

import com.qwict.svkandroid.data.remote.dto.LoginDto
import com.qwict.svkandroid.data.remote.dto.TransportDto
import com.qwict.svkandroid.data.remote.dto.UserDto
import kotlinx.serialization.json.JsonObject
import retrofit2.http.Body
import retrofit2.http.POST

interface SvkApiService {
    @POST("v1/users/login")
    suspend fun login(@Body body: LoginDto): UserDto

    @POST("v1/users/register")
    suspend fun register(@Body body: LoginDto): UserDto

    @POST("v1/transport/")
    suspend fun postTransport(@Body transportEntity: TransportDto): JsonObject
}
