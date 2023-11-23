package com.qwict.svkandroid.domain.use_cases

import android.util.Log
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.data.repository.SvkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateCargoUseCase @Inject constructor(
    private val repo: SvkRepository
){
    operator fun invoke(
        oldCargoNumber : String,
        newCargoNumber : String
    ): Flow<Resource<Boolean>> = flow{
        try {
            Log.i("Update", "Updating cargo")
            emit(Resource.Loading())
            repo.updateCargo(oldCargoNumber, newCargoNumber)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error while updating cargo: ${e.message}"))
        }
    }

}