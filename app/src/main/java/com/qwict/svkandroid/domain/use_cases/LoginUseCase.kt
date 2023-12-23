package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.AuthenticationSingleton
import com.qwict.svkandroid.common.Constants.ROLE_LOADER
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.common.getDecodedPayload
import com.qwict.svkandroid.common.stringRes.ResourceProvider
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

/**
 * Use case responsible for handling user login.
 *
 * @param repo The repository providing access to data sources.
 * @param resourceProvider The resource provider for accessing application resources.
 */
class LoginUseCase @Inject constructor(
    private val repo: SvkRepository,
    private val resourceProvider: ResourceProvider,

) {
    /**
     * Invokes the use case to perform user login.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Flow] emitting [Resource] states containing the user data or an error.
     */
    operator fun invoke(
        email: String,
        password: String,
    ): Flow<Resource<User>> = flow {
//        Log.i("LoginUseCase", "invoke: $email, $password")
        try {
            emit(Resource.Loading())
            val userDto = repo.login(loginDto = LoginDto(email = email, password = password))
            if (userDto.validated) {
                if (isRoleValid(userDto.role)) {
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
                        emit(Resource.Error(resourceProvider.getString(R.string.something_went_wrong_while_validating_your_information_on_your_device_err)))
                    }
                } else {
                    emit(
                        Resource.Error(
                            resourceProvider.getString(
                                R.string.your_role_is_not_allowed_to_use_this_application_err,
                                userDto.role,
                            ),
                        ),
                    )
                }
            } else {
                emit(Resource.Error(resourceProvider.getString(R.string.something_went_wrong_while_validating_your_information_on_the_server_err)))
            }
        } catch (e: HttpException) {
//            Log.e("LoginUseCase", "invoke: ${e.code()}", e)
            when (e.code()) {
                400 -> emit(Resource.Error(resourceProvider.getString(R.string.make_sure_to_fill_out_all_fields_err)))
                401 -> emit(Resource.Error(resourceProvider.getString(R.string.invalid_credentials_err)))
                403 -> emit(Resource.Error(resourceProvider.getString(R.string.not_validated_err)))
                else -> emit(Resource.Error(e.localizedMessage ?: resourceProvider.getString(R.string.an_unexpected_error_occurred_err)))
            }
        } catch (e: IOException) {
            // No internet connection or whatever...
//            Log.e("LoginUseCase", "invoke: ${e.message}", e)
            emit(Resource.Error(resourceProvider.getString(R.string.couldn_t_reach_server_check_your_internet_connection_err)))
        }
    }
}

/**
 * Checks if the user role is valid for using the application.
 *
 * @param role The user's role.
 * @return `true` if the role is valid, `false` otherwise.
 */
private fun isRoleValid(role: String): Boolean {
    return role == ROLE_LOADER
}
