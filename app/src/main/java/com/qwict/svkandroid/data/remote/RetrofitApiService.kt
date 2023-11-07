package com.qwict.svkandroid.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.qwict.svkandroid.common.Constants.BASE_URL
import com.qwict.svkandroid.data.remote.dto.AuthenticatedUserDto
import com.qwict.svkandroid.data.remote.dto.LoginDto
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitApiService {
    // TODO: I should use an interceptor for this but there is no good tutorial that I can find...

    @POST("v1/users/login")
    suspend fun login(@Body body: LoginDto): AuthenticatedUserDto

    @POST("v1/images/")
    suspend fun postImage(@Body image: MultipartBody.Part?): JsonObject

    // TODO: here should be the interceptor at work... (not bearerToken: ...) (everywhere were a token is used)
}

private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).build()

object Api {
    val service: RetrofitApiService = retrofit.create(RetrofitApiService::class.java)
}
