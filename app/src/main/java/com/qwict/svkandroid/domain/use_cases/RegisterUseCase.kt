package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.data.remote.dto.LoginDto
import com.qwict.svkandroid.data.remote.dto.asDomainModel
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repo: SvkRepository,
) {
    operator fun invoke(
        email: String,
        password: String,
    ): Flow<Resource<User>> = flow {
        Log.i("RegisterUseCase", "invoke: $email, $password")
        try {
            emit(Resource.Loading())
            val userDto = repo.register(
                LoginDto(
                    email = email,
                    password = password,
                ),
            )
            emit(Resource.Success(userDto.asDomainModel()))
        } catch (e: HttpException) {
            if (e.code() == 400) {
                emit(Resource.Error("Make sure to fill out all fields."))
            } else if (e.code() == 403) {
                emit(Resource.Error(e.localizedMessage ?: "Registration is temporarily not available."))
            } else if (e.code() == 409) {
                emit(Resource.Error("$email is already in use."))
            } else {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred."))
            }
        } catch (e: IOException) {
            // No internet connection or whatever...
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            Log.e("LoginUseCase", "invoke: ${e.message}", e)
            emit(Resource.Error("The developer didn't do his job..."))
        }
    }
}
