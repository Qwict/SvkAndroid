package com.qwict.svkandroid.domain.use_cases

import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.domain.model.Transport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject

class SelectRouteUseCase @Inject constructor(
    private val repo: SvkRepository,
){
    operator fun invoke(
        routeNumber: Int
    ) : Flow<Resource<Int>> = flow {
        try {
            emit(Resource.Loading())
            if(routeNumber < 0){
                emit(Resource.Error("The given number is not a proper transport number", routeNumber))
            } else {
                repo.insertTransportObject(
                    Transport(
                        routeNumber =  routeNumber,
                        routeDate = Date()
                    )
                )
                println("Inserted in local db")
                emit(Resource.Success(routeNumber))
            }
        } catch (e : Exception) {
            emit(Resource.Error("Failed to save transport locally"))
        }
    }
}