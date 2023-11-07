package com.qwict.svkandroid.data.local.database

import com.qwict.svkandroid.data.local.schema.UserRoomEntity
import kotlinx.coroutines.flow.Flow

interface UserDatabase {
    fun getFlowUserById(id: Int): Flow<UserRoomEntity>
    suspend fun getUserById(id: Int): UserRoomEntity
    suspend fun getUserByEmail(email: String): UserRoomEntity
    suspend fun insert(user: UserRoomEntity)
    suspend fun insertAll(users: List<UserRoomEntity>)
    suspend fun update(user: UserRoomEntity)
    suspend fun delete(user: UserRoomEntity)
}
