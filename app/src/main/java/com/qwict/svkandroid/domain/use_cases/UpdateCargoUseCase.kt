package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.common.stringRes.ResourceProvider
import com.qwict.svkandroid.data.repository.SvkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for updating a cargo with a new cargo number.
 *
 * @param repo The repository providing access to data sources.
 * @param resourceProvider The resource provider for accessing application resources.
 */
class UpdateCargoUseCase @Inject constructor(
    private val repo: SvkRepository,
    private val resourceProvider: ResourceProvider,

) {
    operator fun invoke(
        oldCargoNumber: String,
        newCargoNumber: String,
    ): Flow<Resource<Boolean>> = flow {
        try {
            Log.i("Update", "Updating cargo")
            emit(Resource.Loading())
            repo.updateCargo(oldCargoNumber, newCargoNumber)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    resourceProvider.getString(
                        R.string.unexpected_error_while_updating_cargo_err,
                        e.message,
                    ),
                ),
            )
        }
    }
}
