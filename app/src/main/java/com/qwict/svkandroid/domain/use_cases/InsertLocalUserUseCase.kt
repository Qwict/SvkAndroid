package com.qwict.svkandroid.domain.use_cases // ktlint-disable package-name

import android.util.Log
import com.qwict.svkandroid.common.AuthenticationSingleton
import com.qwict.svkandroid.data.local.saveEncryptedPreference
import com.qwict.svkandroid.data.remote.dto.AuthenticatedUserDto
import com.qwict.svkandroid.domain.model.User

class InsertLocalUserUseCase(
//    private val container: AppContainer,
) {
    suspend operator fun invoke(authenticatedUser: AuthenticatedUserDto): User {
        Log.d(
            "InsertLocalUserUseCase",
            " token : ${authenticatedUser.token},\n" +
                " validated : ${authenticatedUser.validated}, \n" +
                "email : ${authenticatedUser.user.email}",
        )
        // Shared Preferences Part
        saveEncryptedPreference("token", authenticatedUser.token)

        // Singleton Part
        AuthenticationSingleton.validateUser()

        // Database Part -> A local offline user might have a relation with many images...
//        var databaseUserWithImages = authenticatedUser.toDatabaseUserWithImages()
//        container.usersRepository.insert(databaseUserWithPlanets.user)

        var user = User(
            email = authenticatedUser.user.email,
            id = authenticatedUser.user.id,
            name = "",
        )
//        var user = databaseUserWithImages.toUser()
//        Log.d("InsertLocalUserUseCase", "user: ${user.email}")
        return user
    }
}
