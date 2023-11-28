package com.qwict.svkandroid.tasks

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.qwict.svkandroid.common.di.AppModule
import com.qwict.svkandroid.data.local.schema.asImageDto
import com.qwict.svkandroid.data.local.schema.asTransportDto
import java.io.IOException

class SyncWorker(
    private val ctx: Context,
    params: WorkerParameters,
) : CoroutineWorker(ctx, params) {

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
//        syncCargos()
        makeStatusNotification(1, "Syncing", "Successfully synced transports", ctx)
    }

    private suspend fun syncImages() {
        val imagesToSync = database.imageDatabase.getImagesToSync()
        imagesToSync.forEach {
            try {
                Log.i("SyncWorker", "Syncing image: for transport: ${it.routeNumber}")
                // TODO: send image data instead of test string
                val blobResponse = blobApi.postImage(it.imageUuid.toString(), "Test")
                val response = api.postImage(it.asImageDto())
                database.imageDatabase.update(it.copy(isSynced = true))
            } catch (ex: IOException) {
                Log.e("SyncWorker", "Failed to sync image for transport with routeNumber: ${it.routeNumber}", ex)
            }
        }
    }

    private suspend fun syncTransports() {
        val transportsToSync = database.transportDatabase.getTransportsToSync()
        transportsToSync.forEach {
            try {
                Log.i("SyncWorker", "Syncing transport with routeNumber: ${it.transport.routeNumber}")
                val response = api.postTransport(
                    it.asTransportDto(),
                    // TODO: date should probably be this again... Unix time would be great...
                    //  Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC))
                )
                database.transportDatabase.update(it.transport.copy(isSynced = true))
            } catch (ex: IOException) {
                Log.e("SyncWorker", "Failed to sync transport with routeNumber: ${it.transport.routeNumber}", ex)
            }
        }
    }

    // TODO: considering that we are syncing the cargos (and image representations) in transport,
    //      we shouldn't sync the cargos anymore.
//    private suspend fun syncCargos() {
//        val cargosToSync = database.cargoDatabase.getCargosToSync()
//        cargosToSync.forEach {
//            try {
//                Log.i("SyncWorker", "Syncing cargo: ${it.id}")
//                val response = api.postCargo(
//                    CargoDto(
//                        id = it.id,
//                        cargoNumber = it.cargoNumber,
//                        cargoDate = Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)),
//                        // transportId = 3,
//                        routeNumber = it.routeNumber,
//                    ),
//                )
//                database.cargoDatabase.update(it.copy(isSynced = true))
//            } catch (ex: IOException) {
//                Log.e("SyncWorker", "Failed to sync cargo: ${it.id}", ex)
//            }
//        }
//    }
}
