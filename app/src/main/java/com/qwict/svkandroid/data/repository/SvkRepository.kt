package com.qwict.svkandroid.data.repository

import android.net.Uri
import com.qwict.svkandroid.data.local.schema.ImageRoomEntity
import com.qwict.svkandroid.data.local.schema.UserRoomEntity
import com.qwict.svkandroid.data.remote.dto.HealthDto
import com.qwict.svkandroid.data.remote.dto.LoginDto
import com.qwict.svkandroid.data.remote.dto.TransportDto
import com.qwict.svkandroid.data.remote.dto.UserDto
import com.qwict.svkandroid.domain.model.Cargo
import com.qwict.svkandroid.domain.model.Transport
import java.io.File
import java.util.UUID

interface SvkRepository {
    suspend fun getHealth(): HealthDto

    /**
     * @return The AuthenticatedUserDto that represents the user that was registered
     */
    suspend fun login(loginDto: LoginDto): UserDto

    suspend fun insertLocalUser(user: UserDto)
    suspend fun getLocalUserByEmail(email: String): UserRoomEntity

    /**
     * @return The AuthenticatedUserDto that represents the user that was authenticated,
     * Different from login, because the AuthenticatedUserDto is updated with the token.
     */
    suspend fun authenticate(token: String): UserDto

    /**
     * @return The UUID of the image
     */
    suspend fun postImage(image: File): String

    /**
     * @return The TransportDto that represents the Transport that was created in the database
     */
    suspend fun postTransport(transport: Transport): TransportDto

    /**
     * @return The TransportDto that represents the Transport that was patched in the database,
     * this funtion will be called if the transport all ready existed in the database,
     * (a Duplicate entry exception was thrown) and the transport was patched instead.
     */
    suspend fun patchTransport(transport: Transport): TransportDto
    suspend fun register(body: LoginDto): UserDto
    suspend fun insertTransport(transport: Transport)
    suspend fun finishTransportByRouteNumber(routeNumber: String)
    suspend fun insertImage(imageUuid: UUID, userId: Int, routeNumber: String, localUri: Uri)
    suspend fun insertCargo(cargo: Cargo)
    suspend fun updateLocalTransport(transport: Transport)
    suspend fun getActiveTransport(): Transport
    suspend fun syncTransports()
    suspend fun deleteImage(imageUuid: UUID)
    suspend fun updateCargo(oldCargoNumber: String, newCargoNumber: String)
    suspend fun deleteActiveTransport()
    suspend fun finishTransport()
}
