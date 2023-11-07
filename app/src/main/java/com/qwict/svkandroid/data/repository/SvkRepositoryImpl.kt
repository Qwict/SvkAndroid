package com.qwict.svkandroid.data.repository

import android.util.Log
import com.qwict.svkandroid.data.local.RoomContainer
import com.qwict.svkandroid.data.local.schema.UserRoomEntity
import com.qwict.svkandroid.data.remote.RetrofitApiService
import com.qwict.svkandroid.data.remote.dto.AuthenticatedUserDto
import com.qwict.svkandroid.data.remote.dto.HealthDto
import com.qwict.svkandroid.data.remote.dto.LoginDto
import com.qwict.svkandroid.data.remote.dto.TransportDto
import com.qwict.svkandroid.data.remote.dto.UserDto
import com.qwict.svkandroid.data.remote.dto.asRoomEntity
import com.qwict.svkandroid.domain.model.Transport
import java.io.File
import javax.inject.Inject

class SvkRepositoryImpl @Inject constructor(
    private val svkApi: RetrofitApiService,
    private val roomContainer: RoomContainer,
) : SvkRepository {
    override suspend fun getHealth(): HealthDto {
//        return api.getHealth()
        throw NotImplementedError()
    }

    override suspend fun login(body: LoginDto): AuthenticatedUserDto {
        // call api and get user
        val authenticatedUserDto = svkApi.login(body)
        // save user to database (repository pattern applied here)
        Log.d("SvkRepositoryImpl", "login: ${authenticatedUserDto.user}")
        roomContainer.userDatabase.insert(authenticatedUserDto.user.asRoomEntity())
        // return user dto to domain layer
        return authenticatedUserDto
    }

    override suspend fun insertLocalUser(user: UserDto) {
        roomContainer.userDatabase.insert(user.asRoomEntity())
    }

    override suspend fun getLocalUserByEmail(email: String): UserRoomEntity {
        return roomContainer.userDatabase.getUserByEmail(email)
    }

    override suspend fun authenticate(token: String): AuthenticatedUserDto {
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
}
