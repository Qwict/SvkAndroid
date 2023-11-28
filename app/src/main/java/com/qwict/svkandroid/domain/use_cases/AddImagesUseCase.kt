package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.net.Uri
import android.util.Log
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.common.getDecodedPayload
import com.qwict.svkandroid.data.local.getEncryptedPreference
import com.qwict.svkandroid.data.repository.SvkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class AddImagesUseCase @Inject constructor(
    private val repo: SvkRepository,
) {
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
            emit(Resource.Error("Failed to save imageData locally"))
        }
    }
}
