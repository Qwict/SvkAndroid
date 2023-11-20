package com.qwict.svkandroid.data.remote

import com.qwict.svkandroid.data.remote.dto.CargoDto
import com.qwict.svkandroid.data.remote.dto.ImageDto
import com.qwict.svkandroid.data.remote.dto.LoginDto
import com.qwict.svkandroid.data.remote.dto.TransportDto
import com.qwict.svkandroid.data.remote.dto.UserDto
import kotlinx.serialization.json.JsonObject
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitApiService {
    // TODO: I should use an interceptor for this but there is no good tutorial that I can find...

    @POST("v1/users/login")
    suspend fun login(@Body body: LoginDto): UserDto

    @POST("v1/transport/")
    suspend fun postTransport(@Body transportEntity: TransportDto): JsonObject

    @POST("v1/image/")
    suspend fun postImage(@Body imageEntity: ImageDto): JsonObject

    @POST("v1/cargo/")
    suspend fun postCargo(@Body cargoEntity: CargoDto): JsonObject

    // TODO: here should be the interceptor at work... (not bearerToken: ...) (everywhere were a token is used)
}
