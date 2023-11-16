package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.common.AuthenticationSingleton
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.common.getDecodedPayload
import com.qwict.svkandroid.data.local.getEncryptedPreference
import com.qwict.svkandroid.data.local.saveEncryptedPreference
import com.qwict.svkandroid.data.local.schema.asDomainModel
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
            val userDto = repo.login(loginDto = LoginDto(email = email, password = password))
            if (userDto.validated) {
                // Shared Preferences Part, not sure if this should also be in the repository layer...
                // but if so there will be a lot of businiss logic in repository layer;
                // which only serves for repository pattern. I believe
                saveEncryptedPreference("token", userDto.token)

                // Singleton Part
                AuthenticationSingleton.validateUser()

                if (AuthenticationSingleton.isUserAuthenticated) {
//                    emit(Resource.Success(repo.getLocalUserByEmail(authenticatedUser.user.email).asDomainModel()))
                    val email = getDecodedPayload(getEncryptedPreference("token")).email
                    val userRoomEntity = repo.getLocalUserByEmail(email)
                    emit(Resource.Success(userRoomEntity.asDomainModel()))
                } else {
                    emit(Resource.Error("Something went wrong while validating your information on your device."))
                }
            } else {
                emit(Resource.Error("Something went wrong while validating your information on the server."))
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
            Log.e("LoginUseCase", "invoke: ${e.message}", e)
            emit(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }
}
