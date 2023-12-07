package com.qwict.svkandroid.tasks

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.qwict.svkandroid.SvkAndroidApplication
import com.qwict.svkandroid.common.di.AppModule
import com.qwict.svkandroid.data.local.schema.TransportRoomEntity
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
        try {
            sync()
        } catch (ex: Exception) {
            makeStatusNotification(1, "Failed to sync", "Failed to reach server", ctx)
            Log.e("SyncWorker", "Failed to sync", ex)
            return Result.failure()
        }

        return Result.success()
    }

    private suspend fun sync() {
        var isError = false
        var transportsFailed = 0
        var totalTransports = 0
        var transportsFailedList = emptyList<String>()
        makeStatusNotification(1, "Synchronizing", "Synchronizing transports to server...", ctx)
        syncImages()
        syncTransports { isErr, transportsFail, totalTrans, failedTrans ->
            isError = isErr
            transportsFailed += transportsFail
            totalTransports += totalTrans
            if (failedTrans != null) {
                transportsFailedList = transportsFailedList + failedTrans
            }
        }
        if (isError) {
            makeStatusNotification(
                1,
                "Synchronizing",
                "Failed to synchronize $transportsFailed of $totalTransports transports, failed transports are $transportsFailedList",
                ctx,
            )
        } else {
            makeStatusNotification(1, "Synchronizing", "Successfully synchronized transports", ctx)
        }
    }

    private suspend fun syncImages() {
        val imagesToSync = database.imageDatabase.getImagesToSync()
        val resolver = SvkAndroidApplication.appContext.contentResolver
        imagesToSync.forEachIndexed { i, it ->
            try {
                Log.i(
                    "SyncWorker",
                    "Syncing image ${i + 1} for transport ${it.routeNumber} (UUID ${it.imageUuid})",
                )
                val bytes = resolver.openInputStream(it.localUri)?.use { it.readBytes() }
                    ?: throw Error("Failed to read local image")
                val mediaType = resolver.getType(it.localUri)?.toMediaTypeOrNull()
                val requestBody = bytes.toRequestBody(mediaType)
                blobApi.postImage(it.imageUuid.toString(), requestBody)
                database.imageDatabase.update(it.copy(isSynced = true))
            } catch (ex: HttpException) {
                Log.e(
                    "SyncWorker",
                    "Failed to sync image for transport with routeNumber ${it.routeNumber}: ${ex.code()} ${
                        ex.response()?.body()
                    }",
                )
            }
        }
    }

    private suspend fun syncTransports(syncData: (isError: Boolean, transportsFailed: Int, totalTransports: Int, failedTrans: String?) -> Unit) {
        val transportsToSync = database.transportDatabase.getTransportsToSync()
        transportsToSync.forEach {
            try {
                Log.i(
                    "SyncWorker",
                    "Syncing transport with routeNumber: ${it.transport.routeNumber}",
                )
                api.postTransport(
                    it.asTransportDto(),
                    // TODO: date should probably be this again... Unix time would be great...
                    //  Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC))
                )
                database.transportDatabase.update(it.transport.copy(isSynced = true))
                syncData(false, 0, 1, null)
            } catch (ex: HttpException) {
                // If the transport already exists on the server, set it to synced so it doesn't try again
                if (ex.code() == 409) {
                    database.transportDatabase.update(it.transport.copy(isSynced = true))
                } else {
                    syncData(true, 1, 1, it.transport.routeNumber)
                }
                Log.e(
                    "SyncWorker",
                    "Failed to sync transport with routeNumber: ${it.transport.routeNumber}",
                    ex,
                )
            }
        }
    }
}
