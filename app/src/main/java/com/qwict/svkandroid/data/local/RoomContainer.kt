package com.qwict.svkandroid.data.local

import android.content.Context
import com.qwict.svkandroid.data.local.database.CargoDatabase
import com.qwict.svkandroid.data.local.database.CargoDatabaseImpl
import com.qwict.svkandroid.data.local.database.ImageDatabase
import com.qwict.svkandroid.data.local.database.ImageDatabaseImpl
import com.qwict.svkandroid.data.local.database.TransportDatabase
import com.qwict.svkandroid.data.local.database.TransportDatabaseImpl
import com.qwict.svkandroid.data.local.database.UserDatabase
import com.qwict.svkandroid.data.local.database.UserDatabaseImpl
import kotlinx.coroutines.CoroutineScope

/**
 * The `RoomContainer` interface provides access to various databases used in the application.
 */
interface RoomContainer {
    val transportDatabase: TransportDatabase
    val userDatabase: UserDatabase
    val imageDatabase: ImageDatabase
    val cargoDatabase: CargoDatabase
}

/**
 * The `RoomContainerImpl` class implements the [RoomContainer] interface and provides instances
 * of different databases.
 *
 * @property context The application context.
 * @property scope The CoroutineScope for handling asynchronous tasks.
 */
class RoomContainerImpl(private val context: Context, private val scope: CoroutineScope) : RoomContainer {
    override val transportDatabase: TransportDatabase by lazy {
        TransportDatabaseImpl(SvkRoomDatabase.getDatabase(context, scope).transportDao())
    }
    override val userDatabase: UserDatabase by lazy {
        UserDatabaseImpl(SvkRoomDatabase.getDatabase(context, scope).userDao())
    }
    override val imageDatabase: ImageDatabase by lazy {
        ImageDatabaseImpl(SvkRoomDatabase.getDatabase(context, scope).imageDao())
    }
    override val cargoDatabase: CargoDatabase by lazy {
        CargoDatabaseImpl(SvkRoomDatabase.getDatabase(context, scope).cargoDao())
    }
}
