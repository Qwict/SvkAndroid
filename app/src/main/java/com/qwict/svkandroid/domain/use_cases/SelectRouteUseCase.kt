package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.domain.model.Transport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject

class SelectRouteUseCase @Inject constructor(
    private val repo: SvkRepository,
) {
    operator fun invoke(
        routeNumber: String,
    ): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            repo.insertTransportObject(
                Transport(
                    routeNumber = routeNumber,
                    routeDate = Date(),
                ),
            )
            emit(Resource.Success(routeNumber))
            Log.d("SelectRouteUseCase", "Saved transport ($routeNumber) locally")
            emit(Resource.Success(routeNumber))
        } catch (e: Exception) {
            Log.e("SelectRouteUseCase", "Failed to save transport locally", e)
            emit(Resource.Error("Failed to save transport locally"))
        }
    }
}
