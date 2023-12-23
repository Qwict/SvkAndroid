package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.common.stringRes.ResourceProvider
import com.qwict.svkandroid.data.repository.SvkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for deleting the active transport.
 *
 * @param repo The repository providing access to data sources.
 * @param resourceProvider The resource provider for accessing application resources.
 */
class DeleteActiveTransportUseCase @Inject constructor(
    private val repo: SvkRepository,
    private val resourceProvider: ResourceProvider,

) {
    /**
     * Invokes the use case to delete the active transport.
     *
     * @return A [Flow] emitting [Resource] states indicating the operation result.
     */
    operator fun invoke(): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading())
            repo.deleteActiveTransport()
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    resourceProvider.getString(
                        R.string.failed_to_delete_active_transport_err,
                        e.message,
                    ),
                ),
            )
        }
    }
}
