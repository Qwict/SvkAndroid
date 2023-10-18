package com.qwict.svkandroid.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface IUserApi {
    // TODO: What does this even mean? (@JvmSuppressWildcards)
    // TODO: I should use an interceptor for this but there is no good tutorial that I can find...
    @POST("users/actions")
    @JvmSuppressWildcards
    fun handleAction(@Header("authorization") bearerToken: String, @Body body: Map<String, Any>): Call<ResponseBody>

    @POST("users/register")
    @JvmSuppressWildcards
    fun register(@Body body: Map<String, Any>): Call<ResponseBody>

    @POST("users/login")
    @JvmSuppressWildcards
    fun login(@Body body: Map<String, Any>): Call<ResponseBody>

    // TODO: here should be the interceptor at work... (not bearerToken: ...)
    @GET("users/{id}")
    fun getUserById(@Header("authorization") bearerToken: String, @Path("id") id: Int): Call<ResponseBody>
}
