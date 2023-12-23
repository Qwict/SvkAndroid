package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.net.Uri
import android.util.Log
import com.qwict.svkandroid.R
import com.qwict.svkandroid.SvkAndroidApplication
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.common.stringRes.ResourceProvider
import com.qwict.svkandroid.data.repository.SvkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

/**
 * Use case responsible for deleting an image.
 *
 * @param repo The repository providing access to data sources.
 * @param resourceProvider The resource provider for accessing application resources.
 */
class DeleteImageUseCase @Inject constructor(
    private val repo: SvkRepository,
    private val resourceProvider: ResourceProvider,
) {
    /**
     * Invokes the use case to delete an image.
     *
     * @param imageUuid The UUID of the image to be deleted.
     * @param imgUri The Uri of the image to be deleted from the device's media store.
     * @return A [Flow] emitting [Resource] states indicating the operation result.
     */
    operator fun invoke(imageUuid: UUID, imgUri: Uri): Flow<Resource<Boolean>> = flow {
        try {
            val test = imageUuid.toString().replace("-", "").uppercase()
            val uuidTest = UUID.nameUUIDFromBytes(test.toByteArray())
            Log.i("Get", "Getting image with UUID $imageUuid")

            emit(Resource.Loading())
            try {
                val resolver = SvkAndroidApplication.appContext.contentResolver
                val deletedRows = resolver.delete(imgUri, null, null)

                if (deletedRows > 0) {
                    Log.d("ImageDeletion", "Image deleted successfully.")
                } else {
                    Log.e("ImageDeletion", "Failed to delete image.")
                }
            } catch (e: Exception) {
                Log.e("ImageDeletion", "Error deleting image: ${e.message}")
            }

            repo.deleteImage(imageUuid)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            Log.e("Delete", "Failed to delete image")
            emit(Resource.Error(resourceProvider.getString(R.string.failed_to_delete_image_err)))
        }
    }
}
