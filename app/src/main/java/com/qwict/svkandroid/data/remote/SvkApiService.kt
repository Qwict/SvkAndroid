package com.qwict.svkandroid.data.remote

import com.qwict.svkandroid.data.remote.dto.LoginDto
import com.qwict.svkandroid.data.remote.dto.TransportDto
import com.qwict.svkandroid.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit API service interface for SVK API related operations.
 */
interface SvkApiService {
    /**
     * Performs a user login operation.
     *
     * @param body The [LoginDto] containing login credentials.
     * @return The resulting [UserDto] after a successful login.
     */
    @POST("v1/users/login")
    suspend fun login(@Body body: LoginDto): UserDto

    /**
     * Performs a user registration operation.
     *
     * @param body The [LoginDto] containing registration credentials.
     * @return The resulting [UserDto] after a successful registration.
     */
    @POST("v1/users/register")
    suspend fun register(@Body body: LoginDto): UserDto

    /**
     * Posts a transport entity to the server.
     *
     * @param transportEntity The [TransportDto] representing the transport to be posted.
     */
    @POST("v1/transports")
    suspend fun postTransport(@Body transportEntity: TransportDto)
}
