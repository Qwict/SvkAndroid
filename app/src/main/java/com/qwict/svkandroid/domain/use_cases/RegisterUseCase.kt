package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.common.stringRes.ResourceProvider
import com.qwict.svkandroid.data.remote.dto.LoginDto
import com.qwict.svkandroid.data.remote.dto.asDomainModel
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Use case responsible for handling user registration.
 *
 * @param repo The repository providing access to data sources.
 * @param resourceProvider The resource provider for accessing application resources.
 */
class RegisterUseCase @Inject constructor(
    private val repo: SvkRepository,
    private val resourceProvider: ResourceProvider,

) {
    /**
     * Invokes the use case to perform user registration.
     *
     * @param email The user's email address.
     * @param password The user's password.
     * @return A [Flow] emitting [Resource] states containing the user data or an error.
     */
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
                emit(Resource.Error(resourceProvider.getString(R.string.make_sure_to_fill_out_all_fields_err)))
            } else if (e.code() == 403) {
                emit(Resource.Error(e.localizedMessage ?: resourceProvider.getString(R.string.registration_is_temporarily_not_available_err)))
            } else if (e.code() == 409) {
                emit(Resource.Error(resourceProvider.getString(R.string.is_already_in_use_err, email)))
            } else {
                emit(Resource.Error(e.localizedMessage ?: resourceProvider.getString(R.string.an_unexpected_error_occurred_err)))
            }
        } catch (e: IOException) {
            // No internet connection or whatever...
            emit(Resource.Error(resourceProvider.getString(R.string.couldn_t_reach_server_check_your_internet_connection_err)))
        } catch (e: Exception) {
            Log.e("LoginUseCase", "invoke: ${e.message}", e)
            emit(Resource.Error(resourceProvider.getString(R.string.the_developer_didn_t_do_his_job_err)))
        }
    }
}
