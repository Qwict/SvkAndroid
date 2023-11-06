package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.data.remote.dto.LoginDto
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repo: SvkRepository,
) {
    operator fun invoke(
        email: String,
        password: String,
    ): Flow<Resource<User>> = flow {
        Log.i("LoginUseCase", "invoke: $email, $password")
        try {
            emit(Resource.Loading())
            val authenticatedUser = repo.login(LoginDto(email = email, password = password))
            if (authenticatedUser.validated) {
                // Insert user in local database here
//                emit(Resource.Success(InsertLocalUserUseCase(container)(authenticatedUser)))
            }
        } catch (e: HttpException) {
            Log.e("LoginUseCase", "invoke: ${e.code()}", e)
            if (e.code() == 400) {
                emit(Resource.Error("Make sure to fill out all fields."))
            } else if (e.code() == 403) {
                emit(Resource.Error("Invalid credentials."))
            } else {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        } catch (e: IOException) {
            // No internet connection or whatever...
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}
