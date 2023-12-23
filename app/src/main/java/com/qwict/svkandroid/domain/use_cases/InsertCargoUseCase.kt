package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.common.stringRes.ResourceProvider
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.domain.model.Cargo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for inserting a new cargo into the local database.
 *
 * @param repo The repository providing access to data sources.
 * @param resourceProvider The resource provider for accessing application resources.
 */
class InsertCargoUseCase @Inject constructor(
    private val repo: SvkRepository,
    private val resourceProvider: ResourceProvider,
) {
    /**
     * Invokes the use case to insert a new cargo into the local database.
     *
     * @param cargo The [Cargo] object representing the cargo to be inserted.
     * @return A [Flow] emitting [Resource] states containing the cargo number or an error.
     */
    operator fun invoke(
        cargo: Cargo,
    ): Flow<Resource<String>> = flow {
        try {
            Log.i("Insert", "Insert new cargo in the local db")
            emit(Resource.Loading())
            repo.insertCargo(cargo = cargo)
            emit(Resource.Success(cargo.cargoNumber))
        } catch (e: Exception) {
            Log.e("Insert", e.message.toString())
            emit(Resource.Error(resourceProvider.getString(R.string.there_was_an_error_with_inserting_the_cargo_in_the_local_db_err)))
        }
    }
}
