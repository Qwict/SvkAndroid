package com.qwict.svkandroid.tasks

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.qwict.svkandroid.common.di.AppModule
import com.qwict.svkandroid.data.local.RoomContainerImpl
import com.qwict.svkandroid.data.local.schema.asDomainModel
import com.qwict.svkandroid.data.remote.RetrofitApiService
import com.qwict.svkandroid.data.remote.dto.TransportDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date
import java.util.UUID

class SyncWorker(private val ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    private val database = AppModule.provideRoomContainer()
    private val api = AppModule.provideRetrofitApiService()
    override suspend fun doWork(): Result {
        sync()
        return Result.success()
    }

    private suspend fun sync() {
        val transportsToSync = database.transportDatabase.getTransportsToSync()
        makeStatusNotification(1, "Syncing", "Syncing transport to server...", ctx)
        transportsToSync.forEach {
            try {
                Log.i("SyncWorker", "Syncing transport: ${it.id}")
//                val response = api.postTransport(
//                    TransportDto(
//                        routeDate = it.routeDate,
//                        routeNumber = it.routeNumber,
//                        cargoDate = Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)),
//                        driver = it.driver,
//                        licensePlate = it.licensePlate,
//                        imagesBlobUuid = UUID.randomUUID(),
//                    ),
//                )
//                database.transportDatabase.update(it.copy(isSynced = true))
            } catch (ex: IOException) {
                Log.e("SyncWorker", "Failed to sync transport: ${it.id}", ex)
            }
        }
        makeStatusNotification(1, "Syncing", "Successfully synced transports", ctx)
    }
}
