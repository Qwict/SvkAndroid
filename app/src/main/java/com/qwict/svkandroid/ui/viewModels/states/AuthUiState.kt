package com.qwict.svkandroid.ui.viewModels.states

import com.qwict.svkandroid.common.Constants.EMPTY_USER
import com.qwict.svkandroid.domain.model.User

data class AuthUiState(

    val error: String = "",
    val user: User = EMPTY_USER,
    val authState: AuthState = AuthState.UnAuthenticated,
    val isLoading: Boolean = false,
)

sealed interface AuthState {
    data object UnAuthenticated : AuthState
    data object Loading : AuthState
    data object LoggedIn : AuthState
}
