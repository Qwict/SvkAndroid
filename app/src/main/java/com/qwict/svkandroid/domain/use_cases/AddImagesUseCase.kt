package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.net.Uri
import android.util.Log
import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.common.getDecodedPayload
import com.qwict.svkandroid.common.stringRes.ResourceProvider
import com.qwict.svkandroid.data.local.getEncryptedPreference
import com.qwict.svkandroid.data.repository.SvkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

/**
 * Use case responsible for adding images to the local database.
 *
 * @param repo The repository providing access to data sources.
 * @param resourceProvider The resource provider for accessing application resources.
 */
class AddImagesUseCase @Inject constructor(
    private val repo: SvkRepository,
    private val resourceProvider: ResourceProvider,
) {
    /**
     * Invokes the use case to add an image to the local database.
     *
     * @param uuid The UUID of the image.
     * @param routeNumber The route number associated with the image.
     * @param localUri The local URI of the image.
     *
     * @return A [Flow] emitting [Resource] states indicating the operation result.
     */
    operator fun invoke(
        uuid: UUID,
        routeNumber: String,
        localUri: Uri,
    ): Flow<Resource<UUID>> = flow {
        try {
            Log.i("Insert", "Inserting image uuid in local db")
            emit(Resource.Loading())
            val id = getDecodedPayload(getEncryptedPreference("token")).userId
            repo.insertImage(uuid, id, routeNumber, localUri)
            emit(Resource.Success(uuid))
        } catch (e: Exception) {
            Log.e("AddImageUseCase", "Failed to save imageData locally", e)
            emit(Resource.Error(resourceProvider.getString(R.string.failed_to_save_imagedata_locally_err)))
        }
    }
}
