package com.qwict.svkandroid.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.qwict.svkandroid.data.local.RoomContainer
import com.qwict.svkandroid.data.local.schema.CargoRoomEntity
import com.qwict.svkandroid.data.local.schema.ImageRoomEntity
import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    override suspend fun insertImage(imageUuid: UUID, userId: Int, routeNumber: String, localUri: Uri) {
        roomContainer.imageDatabase.insert(
            ImageRoomEntity(
                imageUuid = imageUuid,
                localUri = localUri,
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

        Log.d("ActiveTransport", "Current active transport: ${transportWithoutCargos.asDomainModel()}")

        return transportWithoutCargos.asDomainModel()
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
        withContext(Dispatchers.IO) {
            try {
                val imageRoomEntry = roomContainer.imageDatabase.getImageByImageUuid(imageUuid)
                Log.d("Log", "image gotten from room on $imageUuid: $imageRoomEntry")
                roomContainer.imageDatabase.delete(imageRoomEntry)
            } catch (e: Exception) {
                Log.e("Error", "error in deleteImage : ${e.message!!}")
            }
        }

    }

    override suspend fun updateCargo(oldCargoNumber: String, newCargoNumber: String) {
        withContext(Dispatchers.IO) {
            try {
                val cargoRoomEntry = roomContainer.cargoDatabase.getCargoByCargoNumber(oldCargoNumber)

                roomContainer.cargoDatabase.insert(cargoRoomEntry.copy(cargoNumber = newCargoNumber))
                roomContainer.cargoDatabase.delete(cargoRoomEntry)
            }catch (e: Exception) {
                Log.e("Error", "error in updateCargo : ${e.message!!}")
            }

        }
    }

    override suspend fun deleteActiveTransport() {
        roomContainer.transportDatabase.getActiveTransport().let { transport ->
            val tr = TransportRoomEntity(
                routeNumber = transport.transport.routeNumber,
                driverName = transport.transport.driverName,
                licensePlate = transport.transport.licensePlate
            )
            roomContainer.transportDatabase.delete(tr)
        }
    }

    override suspend fun finishTransport() {
        roomContainer.transportDatabase.getActiveTransport().let { transport ->
            val tr = TransportRoomEntity(
                routeNumber = transport.transport.routeNumber,
                driverName = transport.transport.driverName,
                licensePlate = transport.transport.licensePlate
            )
            roomContainer.transportDatabase.update(tr.copy(isActive = false))
        }
    }
}
