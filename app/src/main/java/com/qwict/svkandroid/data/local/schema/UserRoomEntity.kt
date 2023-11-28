package com.qwict.svkandroid.data.local.schema

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.qwict.svkandroid.domain.model.User

@Entity(
    tableName = "user",
    indices = [
        Index(value = arrayOf("email"), unique = true),
    ],
)
data class UserRoomEntity(
    @ColumnInfo(name = "remote_id")
    @PrimaryKey
    val remoteId: Long,
    val email: String,
    @ColumnInfo(name = "role")
    val role: String,
)

// TODO: Doesn't work? Why?
data class UserRoomEntityWithCargosAndImages(
    @Embedded
    val user: UserRoomEntity,
    @Relation(
        parentColumn = "remote_id",
        entityColumn = "loader_id",
    )
    val cargos: List<CargoRoomEntity>,
    @Relation(
        parentColumn = "remote_id",
        entityColumn = "loader_id",
    )
    val images: List<ImageRoomEntity>,
)

fun UserRoomEntity.asDomainModel() = User(
    email = email,
    role = role,
)
