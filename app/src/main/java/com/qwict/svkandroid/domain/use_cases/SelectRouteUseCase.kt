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
 * Use case responsible for selecting a specific route and initializing a new transport instance.
 *
 * @param repo The repository providing access to data sources.
 * @param resourceProvider The resource provider for accessing application resources.
 */
class SelectRouteUseCase @Inject constructor(
    private val repo: SvkRepository,
    private val resourceProvider: ResourceProvider,
) {
    /**
     * Invokes the use case to select a route and create a new transport instance.
     *
     * @param routeNumber The route number to be associated with the new transport.
     * @return A [Flow] emitting [Resource] states containing the selected route number or an error.
     */
    operator fun invoke(
        routeNumber: String,
    ): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val freshTransport = Transport(routeNumber = routeNumber)
            repo.insertTransport(freshTransport)
            emit(Resource.Success(routeNumber))
            Log.d("SelectRouteUseCase", "Saved transport ($routeNumber) locally")
            emit(Resource.Success(routeNumber))
        } catch (e: Exception) {
            Log.e("SelectRouteUseCase", "Failed to save transport locally", e)
            emit(Resource.Error(resourceProvider.getString(R.string.failed_to_save_transport_locally_err)))
        }
    }
}
