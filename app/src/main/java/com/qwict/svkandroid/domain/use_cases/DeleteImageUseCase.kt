package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.common.stringRes.ResourceProvider
import com.qwict.svkandroid.data.repository.SvkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class DeleteImageUseCase @Inject constructor(
    private val repo: SvkRepository,
    private val resourceProvider: ResourceProvider,
) {
    operator fun invoke(imageUuid: UUID): Flow<Resource<Boolean>> = flow {
        try {
            Log.i("Get", "Getting image with UUID $imageUuid")
            emit(Resource.Loading())
            repo.deleteImage(imageUuid)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            Log.e("Delete", "Failed to delete image")
            emit(Resource.Error(resourceProvider.getString(R.string.failed_to_delete_image_err)))
        }
    }
}
