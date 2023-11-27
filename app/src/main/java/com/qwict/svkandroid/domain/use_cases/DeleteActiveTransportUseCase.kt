package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.data.repository.SvkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteActiveTransportUseCase @Inject constructor(
    private val repo: SvkRepository,
) {
    operator fun invoke(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            repo.deleteActiveTransport()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error("Failed to delete active transport: ${e.message}"))
        }
    }
}
