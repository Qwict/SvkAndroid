package com.qwict.svkandroid.tasks

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.qwict.svkandroid.common.di.AppModule
import com.qwict.svkandroid.data.remote.dto.CargoDto
import com.qwict.svkandroid.data.remote.dto.ImageDto
import com.qwict.svkandroid.data.remote.dto.TransportDto
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date

class SyncWorker(private val ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    private val database = AppModule.provideRoomContainer()
    private val api = AppModule.provideRetrofitApiService()
    private val blobApi = AppModule.provideBlobService()
    override suspend fun doWork(): Result {
        sync()
        return Result.success()
    }

    private suspend fun sync() {
        makeStatusNotification(1, "Syncing", "Syncing transport to server...", ctx)
        syncImages()
        syncTransports()
        syncCargos()
        makeStatusNotification(1, "Syncing", "Successfully synced transports", ctx)
    }

    private suspend fun syncImages() {
        val imagesToSync = database.imageDatabase.getImagesToSync()
        imagesToSync.forEach {
            try {
                Log.i("SyncWorker", "Syncing image: ${it.id}")
                // TODO: send image data instead of test string
                val blobResponse = blobApi.postImage(it.imageUuid.toString(), "Test")
                val response = api.postImage(
                    ImageDto(
                        id = it.id,
                        imageUuid = it.imageUuid.toString(),
                        userId = it.userId,
                        transportId = it.transportId,
                    )
                )
                database.imageDatabase.update(it.copy(isSynced = true))
            } catch (ex: IOException) {
                Log.e("SyncWorker", "Failed to sync image: ${it.id}", ex)
            }
        }
    }

    private suspend fun syncTransports() {
        val transportsToSync = database.transportDatabase.getTransportsToSync()
        transportsToSync.forEach {
            try {
                Log.i("SyncWorker", "Syncing transport: ${it.id}")
                val response = api.postTransport(
                    TransportDto(
                        id = it.id,
                        routeDate = it.routeDate,
                        routeNumber = it.routeNumber,
                        cargoDate = Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)),
                        driver = it.driver,
                        licensePlate = it.licensePlate,
                    ),
                )
                database.transportDatabase.update(it.copy(isSynced = true))
            } catch (ex: IOException) {
                Log.e("SyncWorker", "Failed to sync transport: ${it.id}", ex)
            }
        }
    }

    private suspend fun syncCargos() {
        val cargosToSync = database.cargoDatabase.getCargosToSync()
        cargosToSync.forEach {
            try {
                Log.i("SyncWorker", "Syncing cargo: ${it.id}")
                val response = api.postCargo(
                    CargoDto(
                        id = it.id,
                        cargoNumber = it.cargoNumber,
                        cargoDate = Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)),
                        //transportId = 3,
                        routeNumber = it.routeNumber
                    ),
                )
                database.cargoDatabase.update(it.copy(isSynced = true))
            } catch (ex: IOException) {
                Log.e("SyncWorker", "Failed to sync cargo: ${it.id}", ex)
            }
        }
    }
}
