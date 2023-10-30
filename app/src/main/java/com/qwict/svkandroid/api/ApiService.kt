package com.qwict.svkandroid.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.qwict.svkandroid.common.Constants.BASE_URL
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // TODO: What does this even mean? (@JvmSuppressWildcards). Seems like post requires it...
    // TODO: I should use an interceptor for this but there is no good tutorial that I can find...

    @POST("users/login")
    @JvmSuppressWildcards
    fun login(@Body body: JsonObject): Call<JsonObject>

    // TODO: here should be the interceptor at work... (not bearerToken: ...) (everywhere were a token is used)
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(
        Json.asConverterFactory("application/json".toMediaType()),
    )
    .baseUrl(BASE_URL).build()

object Api {
    val service: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
