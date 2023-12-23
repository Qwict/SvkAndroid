package com.qwict.svkandroid

import android.net.Uri
import com.qwict.svkandroid.data.local.schema.CargoRoomEntity
import com.qwict.svkandroid.data.local.schema.ImageRoomEntity
import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
import com.qwict.svkandroid.data.local.schema.UserRoomEntity
import com.qwict.svkandroid.data.local.schema.asDomainModel
import com.qwict.svkandroid.data.remote.dto.HealthDto
import com.qwict.svkandroid.data.remote.dto.LoginDto
import com.qwict.svkandroid.data.remote.dto.TransportDto
import com.qwict.svkandroid.data.remote.dto.UserDto
import com.qwict.svkandroid.data.remote.dto.asRoomEntity
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.domain.model.Cargo
import com.qwict.svkandroid.domain.model.Transport
import com.qwict.svkandroid.domain.model.asRoomEntity
import okhttp3.internal.UTC
import java.io.File
import java.time.LocalDate
import java.util.UUID

class FakeSvkRepository : SvkRepository {
    private var users = listOf(
        UserRoomEntity(
            remoteId = 1,
            email = "test@test.com",
            role = "Loader",
        ),
    )
    private var transports = emptyList<TransportRoomEntity>()
    private var cargos = emptyList<CargoRoomEntity>()
    private var images = emptyList<ImageRoomEntity>()
    override suspend fun getHealth(): HealthDto {
        return HealthDto(
            env = "TEST",
            name = "svk-api",
            version = "1.0.0",
        )
    }

    override suspend fun login(loginDto: LoginDto): UserDto {
        if (loginDto.email.isBlank()) {
            throw Exception("Email is blank")
        }
        return UserDto()
    }

    override suspend fun insertLocalUser(user: UserDto) {
        users = users.plus(user.asRoomEntity())
    }

    override suspend fun getLocalUserByEmail(email: String): UserRoomEntity {
        return users.first { it.email == email }
    }

    override suspend fun authenticate(token: String): UserDto {
        return UserDto()
    }

    override suspend fun postImage(image: File): String {
        return image.name
    }

    override suspend fun postTransport(transport: Transport): TransportDto {
        return TransportDto(
            routeNumber = transport.routeNumber,
            images = emptyList(),
            createdAt = LocalDate.now().toEpochDay(),
            cargos = emptyList(),
            driverName = transport.driverName,
            licensePlate = transport.licensePlate,

            )
    }

    override suspend fun patchTransport(transport: Transport): TransportDto {
        return TransportDto(
            routeNumber = transport.routeNumber,
            images = emptyList(),
            createdAt = LocalDate.now().toEpochDay(),
            cargos = emptyList(),
            driverName = transport.driverName,
            licensePlate = transport.licensePlate,

            )
    }

    override suspend fun register(body: LoginDto): UserDto {
        if (body.email.isBlank()) {
            throw Exception("Email is blank")
        }
        return UserDto()
    }

    override suspend fun insertTransport(transport: Transport) {
        transports = transports.plus(transport.asRoomEntity())
    }

    override suspend fun finishTransportByRouteNumber(routeNumber: String) {
        if (routeNumber.isBlank()) {
            throw Exception("Route number is blank")
        }
        var transport = transports.first { it.routeNumber == routeNumber }

        transports = transports.minus(transport).plus(transport.copy(isActive = false))
    }

    override suspend fun insertImage(
        imageUuid: UUID,
        userId: Int,
        routeNumber: String,
        localUri: Uri,
    ) {
        images = images.plus(
            ImageRoomEntity(
                imageUuid = imageUuid,
                routeNumber = routeNumber,
                localUri = localUri,
                createdAt = LocalDate.now().toEpochDay(),
                loaderId = userId,
            ),
        )
    }

    override suspend fun insertCargo(cargo: Cargo) {
        cargos = cargos.plus(cargo.asRoomEntity())
    }

    override suspend fun updateLocalTransport(transport: Transport) {
    }

    override suspend fun getActiveTransport(): Transport {
        return transports.filter { it.isActive }.first().asDomainModel()
    }

    override suspend fun syncTransports() {
    }

    override suspend fun deleteImage(imageUuid: UUID) {
        images = images.minus(images.first { it.imageUuid == imageUuid })
    }

    override suspend fun updateCargo(oldCargoNumber: String, newCargoNumber: String) {
        cargos = cargos.minus(cargos.first { it.cargoNumber == oldCargoNumber }).plus(
            cargos.first { it.cargoNumber == oldCargoNumber }.copy(cargoNumber = newCargoNumber),
        )
    }

    override suspend fun deleteActiveTransport() {
        transports = transports.minus(transports.first { it.isActive })
    }

    override suspend fun finishTransport() {
        if (transports.isEmpty()) {
            throw Exception("No active transport")
        }
        if (transports.first().routeNumber.isBlank()) {
            throw Exception("Route number is blank")
        }


    }
}
