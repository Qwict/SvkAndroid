package com.qwict.svkandroid.data.remote

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Retrofit API service interface for Blob operations, used for uploading images.
 */
interface BlobApiService {
    @POST("{id}")
    suspend fun postImage(@Path("id") id: String, @Body image: RequestBody)
}
