package com.qwict.svkandroid.data.repository

import android.net.Uri
import com.qwict.svkandroid.data.local.schema.UserRoomEntity
import com.qwict.svkandroid.data.remote.dto.HealthDto
import com.qwict.svkandroid.data.remote.dto.LoginDto
import com.qwict.svkandroid.data.remote.dto.TransportDto
import com.qwict.svkandroid.data.remote.dto.UserDto
import com.qwict.svkandroid.domain.model.Cargo
import com.qwict.svkandroid.domain.model.Transport
import java.io.File
import java.util.UUID

/**
 * Repository interface providing a contract for data operations related to the application.
 */
interface SvkRepository {
    /**
     * Retrieves the health information from the server.
     */
    suspend fun getHealth(): HealthDto

    /**
     * Attempts to authenticate the user based on the provided [LoginDto].
     *
     * @param loginDto The login data.
     * @return The user information upon successful authentication.
     */
    suspend fun login(loginDto: LoginDto): UserDto

    /**
     * Inserts a local user into the database.
     *
     * @param user The user data to be inserted.
     */
    suspend fun insertLocalUser(user: UserDto)

    /**
     * Retrieves a local user by their email address.
     *
     * @param email The email address of the user.
     * @return The [UserRoomEntity] representing the local user.
     */
    suspend fun getLocalUserByEmail(email: String): UserRoomEntity

    /**
     * Authenticates the user based on the provided token.
     *
     * @param token The authentication token.
     * @return The user information upon successful authentication.
     */
    suspend fun authenticate(token: String): UserDto

    /**
     * Uploads an image to the server.
     *
     * @param image The [File] representing the image to be uploaded.
     * @return The identifier of the uploaded image.
     */
    suspend fun postImage(image: File): String

    /**
     * Posts a transport to the server.
     *
     * @param transport The [Transport] object representing the transport data.
     * @return The [TransportDto] representing the posted transport.
     */
    suspend fun postTransport(transport: Transport): TransportDto

    /**
     * Updates an existing transport on the server.
     *
     * @param transport The [Transport] object representing the updated transport data.
     * @return The [TransportDto] representing the updated transport.
     */
    suspend fun patchTransport(transport: Transport): TransportDto

    /**
     * Registers a new user on the server.
     *
     * @param body The [LoginDto] containing registration data.
     * @return The user information upon successful registration.
     */
    suspend fun register(body: LoginDto): UserDto

    /**
     * Inserts a transport locally into the database.
     *
     * @param transport The [Transport] object representing the transport data to be inserted.
     */
    suspend fun insertTransport(transport: Transport)

    /**
     * Marks the transport with the specified route number as finished on the server.
     *
     * @param routeNumber The route number of the transport to be finished.
     */
    suspend fun finishTransportByRouteNumber(routeNumber: String)

    /**
     * Inserts an image locally into the database.
     *
     * @param imageUuid The UUID of the image.
     * @param userId The ID of the user associated with the image.
     * @param routeNumber The route number associated with the image.
     * @param localUri The local URI of the image.
     */
    suspend fun insertImage(imageUuid: UUID, userId: Int, routeNumber: String, localUri: Uri)

    /**
     * Inserts a cargo locally into the database.
     *
     * @param cargo The [Cargo] object representing the cargo data to be inserted.
     */
    suspend fun insertCargo(cargo: Cargo)

    /**
     * Updates a local transport in the database.
     *
     * @param transport The [Transport] object representing the updated transport data.
     */
    suspend fun updateLocalTransport(transport: Transport)

    /**
     * Retrieves the active transport from the local database.
     *
     * @return The active [Transport] object.
     */
    suspend fun getActiveTransport(): Transport

    /**
     * Synchronizes transports with the server.
     */
    suspend fun syncTransports()

    /**
     * Deletes the image with the specified UUID.
     *
     * @param imageUuid The UUID of the image to be deleted.
     */
    suspend fun deleteImage(imageUuid: UUID)

    /**
     * Updates the cargo number for a given transport.
     *
     * @param oldCargoNumber The old cargo number to be updated.
     * @param newCargoNumber The new cargo number to replace the old one.
     */
    suspend fun updateCargo(oldCargoNumber: String, newCargoNumber: String)

    /**
     * Deletes the active transport from the local database.
     */
    suspend fun deleteActiveTransport()

    /**
     * Marks the active transport as finished on the server.
     */
    suspend fun finishTransport()
}
