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

class SelectRouteUseCase @Inject constructor(
    private val repo: SvkRepository,
    private val resourceProvider: ResourceProvider,
) {
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
