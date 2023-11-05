package com.qwict.svkandroid.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.qwict.svkandroid.common.Constants.BASE_URL
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // TODO: I should use an interceptor for this but there is no good tutorial that I can find...

    @POST("user/login")
    suspend fun login(@Body body: JsonObject): JsonObject

    // TODO: here should be the interceptor at work... (not bearerToken: ...) (everywhere were a token is used)
}

private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).build()


object Api {
    val service: ApiService = retrofit.create(ApiService::class.java)
}
