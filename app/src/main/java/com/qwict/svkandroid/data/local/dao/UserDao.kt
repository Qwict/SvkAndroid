package com.qwict.svkandroid.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.qwict.svkandroid.data.local.schema.UserRoomEntity

/**
 * The Data Access Object for the UserRoomEntity class.
 */
@Dao
interface UserDao {
    /**
     * Inserts a single user into the local database. If a conflict occurs, the existing user is replaced.
     *
     * @param user The user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserRoomEntity)

    /**
     * Inserts a list of users into the local database. If a conflict occurs, the existing users are replaced.
     *
     * @param users The list of users to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserRoomEntity>)

    /**
     * Updates a user in the local database.
     *
     * @param user The user to be updated.
     */
    @Update
    suspend fun update(user: UserRoomEntity)

    /**
     * Deletes a user from the local database.
     *
     * @param user The user to be deleted.
     */
    @Delete
    suspend fun delete(user: UserRoomEntity)

    /**
     * Retrieves a user from the local database by remote ID.
     *
     * @param remoteId The remote ID of the user to retrieve.
     * @return The user with the specified remote ID.
     */
    @Query("SELECT * FROM user WHERE remote_id = :remoteId")
    suspend fun getUserByRemoteId(remoteId: Int): UserRoomEntity

    /**
     * Retrieves a user from the local database by email.
     *
     * @param email The email of the user to retrieve.
     * @return The user with the specified email.
     */
    @Query("SELECT * FROM user WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserRoomEntity

//    @Transaction
//    @Query("SELECT * FROM user")
//    fun getUserWithCargosAndImages(): List<UserRoomEntityWithCargosAndImages>
}
