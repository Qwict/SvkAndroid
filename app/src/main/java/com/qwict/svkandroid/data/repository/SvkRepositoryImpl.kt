package com.qwict.svkandroid.data.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.qwict.svkandroid.data.local.RoomContainer
import com.qwict.svkandroid.data.local.schema.CargoRoomEntity
import com.qwict.svkandroid.data.local.schema.ImageRoomEntity
import com.qwict.svkandroid.data.local.schema.UserRoomEntity
import com.qwict.svkandroid.data.local.schema.asDomainModel
import com.qwict.svkandroid.data.remote.SvkApiService
import com.qwict.svkandroid.data.remote.dto.HealthDto
import com.qwict.svkandroid.data.remote.dto.LoginDto
import com.qwict.svkandroid.data.remote.dto.TransportDto
import com.qwict.svkandroid.data.remote.dto.UserDto
import com.qwict.svkandroid.data.remote.dto.asRoomEntity
import com.qwict.svkandroid.domain.model.Cargo
import com.qwict.svkandroid.domain.model.Transport
import com.qwict.svkandroid.domain.model.asRoomEntity
import com.qwict.svkandroid.tasks.SyncWorker
import java.io.File
import java.util.UUID
import javax.inject.Inject

class SvkRepositoryImpl @Inject constructor(
    private val svkApi: SvkApiService,
    private val roomContainer: RoomContainer,
    private val ctx: Context,
) : SvkRepository {
    override suspend fun getHealth(): HealthDto {
//        return api.getHealth()
        throw NotImplementedError()
    }

    override suspend fun login(loginDto: LoginDto): UserDto {
        // call api and get user
        val userDto = svkApi.login(loginDto)
        if (userDto.validated) {
            // save user to database (repository pattern applied here)
            roomContainer.userDatabase.insert(userDto.asRoomEntity())
        }

        // return user dto to domain layer
        return userDto
    }

    override suspend fun register(body: LoginDto): UserDto {
        // call api and get user
        return svkApi.register(body)
    }

    override suspend fun insertLocalUser(user: UserDto) {
        roomContainer.userDatabase.insert(user.asRoomEntity())
    }

    override suspend fun getLocalUserByEmail(email: String): UserRoomEntity {
        return roomContainer.userDatabase.getUserByEmail(email)
    }

    override suspend fun authenticate(token: String): UserDto {
//        return api.authenticate(token)
        throw NotImplementedError()
    }

    override suspend fun postImage(image: File): String {
//        return svkApi.postImage(image)
        throw NotImplementedError()
    }

    override suspend fun postTransport(transport: Transport): TransportDto {
        throw NotImplementedError()
    }

    override suspend fun patchTransport(transport: Transport): TransportDto {
        throw NotImplementedError()
    }

    override suspend fun insertTransport(transport: Transport) {
        roomContainer.transportDatabase.insert(transport.asRoomEntity())
    }

    override suspend fun finishTransportByRouteNumber(routeNumber: String) {
        roomContainer.transportDatabase.getTransportByRouteNumber(routeNumber)
    }

    override suspend fun insertCargo(cargo: Cargo) {
        roomContainer.cargoDatabase.insert(
            CargoRoomEntity(
                cargoNumber = cargo.cargoNumber,
                loaderId = cargo.loaderId,
                routeNumber = cargo.routeNumber,
            ),
        )
    }

    override suspend fun insertImage(imageUuid: UUID, userId: Int, routeNumber: String) {
        roomContainer.imageDatabase.insert(
            ImageRoomEntity(
                imageUuid = imageUuid,
                loaderId = userId,
                routeNumber = routeNumber,
            ),
        )

        // TODO: don't think this is needed anymore; because you give routeNumber to Image (foreign key)
//        val transport = getActiveTransport()
//        val newTransport = TransportRoomEntityWithImages(
//            transport.asRoomEntity(),
//            listOf(imageRoom),
//        )
//
//        roomContainer.transportDatabase.insert(newTransport.transport)
    }
    override suspend fun updateLocalTransport(transport: Transport) {
        roomContainer.transportDatabase.update(transport.asRoomEntity())
    }

    override suspend fun getActiveTransport(): Transport {
        // TODO: this is the wrong way to do this... need to fix this; should be a single call to the database
        // that returns the transport with the cargos (and images) attached

        val transportWithoutCargos = roomContainer.transportDatabase.getActiveTransport()
        val cargos = roomContainer.cargoDatabase.getCargosByRouteNumber(transportWithoutCargos.routeNumber)
            .map { it.asDomainModel() }
        return transportWithoutCargos.asDomainModel().copy(cargos = cargos)
    }

    override suspend fun syncTransports() {
        val workRequest = OneTimeWorkRequest.Builder(SyncWorker::class.java).setConstraints(
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build(),
        )
            .build()
        val workManager = WorkManager.getInstance(ctx)
        workManager.enqueueUniqueWork("SYNC_TRANSPORTS", ExistingWorkPolicy.KEEP, workRequest)
    }

    override suspend fun deleteImage(imageUuid: UUID) {
        val imageRoomEntry = roomContainer.imageDatabase.getImageByImageUuid(imageUuid)
        roomContainer.imageDatabase.delete(imageRoomEntry)
    }

    override suspend fun updateCargo(oldCargoNumber: String, newCargoNumber: String) {
        val cargoRoomEntry = roomContainer.cargoDatabase.getCargoByCargoNumber(oldCargoNumber)
        roomContainer.cargoDatabase.update(cargoRoomEntry.copy(cargoNumber = newCargoNumber))
    }

    override suspend fun deleteActiveTransport() {
        roomContainer.transportDatabase.getActiveTransport().let { transport ->
            roomContainer.transportDatabase.delete(transport)
        }
    }

    override suspend fun finishTransport() {
        roomContainer.transportDatabase.getActiveTransport().let { transport ->
            roomContainer.transportDatabase.update(transport.copy(isActive = false))
        }
    }
}
