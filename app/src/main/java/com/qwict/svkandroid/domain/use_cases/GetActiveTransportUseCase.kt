package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.common.stringRes.ResourceProvider
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.domain.model.Transport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for retrieving the active transport.
 *
 * @param repo The repository providing access to data sources.
 * @param resourceProvider The resource provider for accessing application resources.
 */
class GetActiveTransportUseCase @Inject constructor(
    private val repo: SvkRepository,
    private val resourceProvider: ResourceProvider,
) {
    /**
     * Invokes the use case to retrieve the active transport.
     *
     * @return A [Flow] emitting [Resource] states containing the active [Transport] or an error.
     */
    operator fun invoke(): Flow<Resource<Transport?>> = flow {
        emit(Resource.Loading())
        try {
            val activeTransport = repo.getActiveTransport()
            emit(Resource.Success(activeTransport))
        } catch (nullPointerException: NullPointerException) {
            Log.e("GetActiveTransport", "invoke: ", nullPointerException)
            emit(Resource.Error(resourceProvider.getString(R.string.no_active_transport_was_found_err)))
        } catch (e: Exception) {
            Log.e("GetActiveTransport", "invoke: ", e)
            emit(Resource.Error("Something went wrong while getting active transport."))
        }
    }
}
