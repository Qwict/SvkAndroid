package com.qwict.svkandroid.data.repository

import com.qwict.svkandroid.data.remote.ApiService
import com.qwict.svkandroid.data.remote.dto.AuthenticatedUserDto
import com.qwict.svkandroid.data.remote.dto.HealthDto
import com.qwict.svkandroid.data.remote.dto.LoginDto
import javax.inject.Inject

class SvkRepositoryImpl @Inject constructor(
    private val api: ApiService,
) : SvkRepository {
    override suspend fun getHealth(): HealthDto {
        throw NotImplementedError()
//        return api.getHealth()
    }

    override suspend fun login(body: LoginDto): AuthenticatedUserDto {
        throw NotImplementedError()
//        return api.login(body)
    }

    override suspend fun authenticate(token: String): AuthenticatedUserDto {
        throw NotImplementedError()
//        return api.authenticate(token)
    }
}
