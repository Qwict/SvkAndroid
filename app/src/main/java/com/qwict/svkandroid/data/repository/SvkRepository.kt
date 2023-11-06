package com.qwict.svkandroid.data.repository

import com.qwict.svkandroid.data.remote.dto.AuthenticatedUserDto
import com.qwict.svkandroid.data.remote.dto.HealthDto
import com.qwict.svkandroid.data.remote.dto.LoginDto

interface SvkRepository {
    suspend fun getHealth(): HealthDto
    suspend fun login(body: LoginDto): AuthenticatedUserDto
    suspend fun authenticate(token: String): AuthenticatedUserDto
}
