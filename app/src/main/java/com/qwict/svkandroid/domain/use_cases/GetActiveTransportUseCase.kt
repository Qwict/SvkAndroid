package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.domain.model.Transport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetActiveTransportUseCase @Inject constructor(
    private val repo: SvkRepository,
) {
    operator fun invoke(): Flow<Resource<Transport>> = flow {
        emit(Resource.Loading())
        try {
            val activeTransport = repo.getActiveTransport()
            emit(Resource.Success(activeTransport))
        } catch (e: Exception) {
            Log.e("GetActiveTransport", "invoke: ", e)
            emit(Resource.Error("Something went wrong while getting active transport."))
        }
    }
}
