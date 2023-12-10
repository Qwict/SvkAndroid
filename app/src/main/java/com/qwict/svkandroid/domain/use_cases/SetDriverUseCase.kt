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

class SetDriverUseCase @Inject constructor(
    private val repo: SvkRepository,
    private val resourceProvider: ResourceProvider,

) {
    operator fun invoke(
        routeNumber: String,
        licensePlate: String,
        driver: String,
    ): Flow<Resource<Transport>> = flow {
        try {
            Log.i("Update", "Updating transport in room with licenseplate and drivername")
            emit(Resource.Loading())
            Log.d("Update", "to find the log nr: $routeNumber, lp: $licensePlate, dr: $driver")
            val transport = Transport(
                routeNumber = routeNumber,
                licensePlate = licensePlate,
                driverName = driver,
            )
            repo.insertTransport(
                transport,
            )
            emit(Resource.Success(transport))
        } catch (e: Exception) {
            Log.e("SetLoaderUseCase", "Failed to set a driver and licenseplatenumber", e)
            emit(Resource.Error(resourceProvider.getString(R.string.failed_to_set_a_driver_and_licenseplatenumber_err)))
        }
    }
}
