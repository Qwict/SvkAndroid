package com.qwict.svkandroid.data.local.database

import com.qwict.svkandroid.data.local.dao.UserDao
import com.qwict.svkandroid.data.local.schema.UserRoomEntity
import kotlinx.coroutines.flow.Flow

class UserDatabaseImpl(private val userDao: UserDao) : UserDatabase {
    override fun getFlowUserById(id: Int): Flow<UserRoomEntity> {
        return userDao.getUserFlowById(id)
    }
    override suspend fun getUserById(id: Int): UserRoomEntity {
        return userDao.getUserById(id)
    }

    override suspend fun getUserByEmail(email: String): UserRoomEntity {
        return userDao.getUserByEmail(email)
    }

    override suspend fun insert(user: UserRoomEntity) {
        userDao.insert(user)
    }
    override suspend fun insertAll(users: List<UserRoomEntity>) {
        userDao.insertAll(users)
    }
    override suspend fun update(user: UserRoomEntity) {
        userDao.update(user)
    }
    override suspend fun delete(user: UserRoomEntity) {
        userDao.delete(user)
    }
}
