package com.qwict.svkandroid.data.local.schema

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.qwict.svkandroid.domain.model.User

@Entity(
    tableName = "user",
    indices = [
        Index(
            value = arrayOf("email"),
            unique = true,
        ),
    ],
)
data class UserRoomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
//    @ColumnInfo(name = "remote_id") var remoteId: Int = 0, // could be used somewhere ... maybe add it later?
    val email: String = "",
    @ColumnInfo(name = "first_name")
    val firstName: String = "",
    @ColumnInfo(name = "last_name")
    val lastName: String = "",
    @ColumnInfo(name = "role")
    val role: String = "",
    @ColumnInfo(name = "remote_id")
    val remoteId: Long = 0,
)

// TODO: Doesn't work?
// data class UserRoomEntityWithCargosAndImages(
//    val user: UserRoomEntity,
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "id",
//    )
//    val cargos: List<CargoRoomEntity>,
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "id",
//    )
//    val images: List<ImageRoomEntity>,
// )

data class UserRoomEntityWithCargos(
    val user: UserRoomEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val cargos: List<CargoRoomEntity>,
)

data class UserRoomEntityWithImages(
    val user: UserRoomEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
    )
    val images: List<ImageRoomEntity>,
)

fun UserRoomEntity.asDomainModel() = User(
    email = email,
    firstName = firstName,
    lastName = lastName,
    role = role,
)

// Could insert seeds here to populate the database with some data
fun populateUsers(): List<UserRoomEntity> {
    return listOf()
}
