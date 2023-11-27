package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.domain.model.Cargo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertCargoUseCase @Inject constructor(
    private val repo: SvkRepository,
) {
    operator fun invoke(
        cargo: Cargo,
        routeNumber: String,
    ): Flow<Resource<String>> = flow {
        try {
            Log.i("Insert", "Insert new cargo in the local db")
            emit(Resource.Loading())
            repo.insertCargoObject(
                cargo = cargo,
                routeNumber = routeNumber,
            )
            emit(Resource.Success(cargo.cargoNumber))
        } catch (e: Exception) {
            Log.e("Insert", e.message.toString())
            emit(Resource.Error("There was an error with inserting the cargo in the local db"))
        }
    }
}
