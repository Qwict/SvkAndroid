package com.qwict.svkandroid.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.qwict.svkandroid.data.local.converter.DateConverter
import com.qwict.svkandroid.data.local.dao.CargoDao
import com.qwict.svkandroid.data.local.dao.ImageDao
import com.qwict.svkandroid.data.local.dao.TransportDao
import com.qwict.svkandroid.data.local.dao.UserDao
import com.qwict.svkandroid.data.local.schema.CargoRoomEntity
import com.qwict.svkandroid.data.local.schema.ImageRoomEntity
import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
import com.qwict.svkandroid.data.local.schema.UserRoomEntity
import com.qwict.svkandroid.data.local.schema.populateCargos
import com.qwict.svkandroid.data.local.schema.populateImages
import com.qwict.svkandroid.data.local.schema.populateTransports
import com.qwict.svkandroid.data.local.schema.populateUsers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [TransportRoomEntity::class, CargoRoomEntity::class, UserRoomEntity::class, ImageRoomEntity::class],
    // change this to version+1 when you change the schema
    version = 4,
    exportSchema = false,
)
@TypeConverters(DateConverter::class)
abstract class SvkRoomDatabase : RoomDatabase() {

    abstract fun transportDao(): TransportDao
    abstract fun cargoDao(): CargoDao
    abstract fun userDao(): UserDao
    abstract fun imageDao(): ImageDao

    companion object {
        @Volatile
        private var Instance: SvkRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): SvkRoomDatabase {
            Log.d("SvkRoomDatabase", "Creating database")
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    SvkRoomDatabase::class.java,
                    "svk_database",
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        // Use a coroutine to insert data if we would have seeds...
                        val transportDao = Instance?.transportDao()
                        val userDao = Instance?.userDao()
                        val cargoDao = Instance?.cargoDao()
                        val imageDao = Instance?.imageDao()
                        Log.i("SvkRoomDatabase", "Inserting all transports, cargos, users and images")
                        scope.launch {
                            transportDao?.insertAll(populateTransports())
                            cargoDao?.insertAll(populateCargos())
                            userDao?.insertAll(populateUsers())
                            imageDao?.insertAll(populateImages())
                        }
                    }
                })
//                     Will destroy the database on schema change (uncomment this when error is thrown)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
