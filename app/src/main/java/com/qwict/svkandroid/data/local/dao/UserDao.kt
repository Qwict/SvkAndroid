package com.qwict.svkandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.qwict.svkandroid.data.local.schema.UserRoomEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserRoomEntity>)

    @Update
    suspend fun update(user: UserRoomEntity)

    @Delete
    suspend fun delete(user: UserRoomEntity)

    @Query("SELECT * FROM user WHERE remote_id = :remoteId")
    suspend fun getUserByRemoteId(remoteId: Int): UserRoomEntity

    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserRoomEntity

//    TODO: Figure out how to get this to work
//    @Transaction
//    @Query("SELECT * FROM user")
//    fun getUserWithCargosAndImages(): List<UserRoomEntityWithCargosAndImages>
}
