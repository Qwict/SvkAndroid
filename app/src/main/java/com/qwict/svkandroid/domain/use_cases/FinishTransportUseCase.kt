package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.domain.model.Transport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FinishTransportUseCase @Inject constructor(
    private val repo: SvkRepository,
) {
    operator fun invoke(trans: Transport): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            repo.insertTransport(trans)
            repo.finishTransport()
            repo.syncTransports()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            Log.e("FinishTransportUseCase", "Failed to finish transport: ${e.message}")
            emit(Resource.Error("Failed to delete active transport: ${e.message}"))
        }
    }
}
