package com.qwict.svkandroid.domain.use_cases

import android.util.Log
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.data.repository.SvkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class DeleteImageUseCase @Inject constructor(
    private val repo: SvkRepository
) {
    operator fun invoke(
        uuid: UUID
    ): Flow<Resource<Boolean>> = flow  {
        try {
            Log.i("Get", "Getting image with UUID $uuid")
            emit(Resource.Loading())
            repo.deleteImage(uuid.toString())
            emit(Resource.Success(true))
        } catch (e : Exception){
            Log.e("Delete", "Failed to delete image")
            emit(Resource.Error("Failed to delete image"))
        }
    }
}