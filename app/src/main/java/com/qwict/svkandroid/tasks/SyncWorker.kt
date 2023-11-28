package com.qwict.svkandroid.tasks

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.qwict.svkandroid.SvkAndroidApplication
import com.qwict.svkandroid.common.di.AppModule
import com.qwict.svkandroid.data.local.schema.asTransportDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class SyncWorker(
    private val ctx: Context,
    params: WorkerParameters,
) : CoroutineWorker(ctx, params) {

    private val database = AppModule.provideRoomContainer()
    private val api = AppModule.provideSvkApiService()
    private val blobApi = AppModule.provideBlobService()
    override suspend fun doWork(): Result {
        sync()
        return Result.success()
    }

    private suspend fun sync() {
        makeStatusNotification(1, "Syncing", "Syncing transports to server...", ctx)
        syncImages()
        syncTransports()
        makeStatusNotification(1, "Syncing", "Successfully synced transports", ctx)
    }

    private suspend fun syncImages() {
        val imagesToSync = database.imageDatabase.getImagesToSync()
        val resolver = SvkAndroidApplication.appContext.contentResolver
        imagesToSync.forEachIndexed { i, it ->
            try {
                Log.i("SyncWorker", "Syncing image ${i+1} for transport ${it.routeNumber} (UUID ${it.imageUuid})")
                val bytes = resolver.openInputStream(it.localUri)?.use { it.readBytes() }
                    ?: throw Error("Failed to read local image")
                val mediaType = resolver.getType(it.localUri)?.toMediaTypeOrNull()
                val requestBody = bytes.toRequestBody(mediaType)
                blobApi.postImage(it.imageUuid.toString(), requestBody)
                database.imageDatabase.update(it.copy(isSynced = true))
            } catch (ex: HttpException) {
                Log.e("SyncWorker", "Failed to sync image for transport with routeNumber ${it.routeNumber}: ${ex.code()} ${ex.response()?.body()}")
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
            } catch (ex: HttpException) {
                Log.e("SyncWorker", "Failed to sync transport with routeNumber: ${it.transport.routeNumber}", ex)
            }
        }
    }
}
