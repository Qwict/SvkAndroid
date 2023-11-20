package com.qwict.svkandroid.data.remote

import com.qwict.svkandroid.data.remote.dto.TransportDto
import kotlinx.serialization.json.JsonObject
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BlobApiService {
    @POST("{id}")
    suspend fun postImage(@Path("id") id: String, @Body image: String): JsonObject

    @GET("{id}")
    suspend fun getImage(@Body transportEntity: TransportDto): MultipartBody.Part
}
