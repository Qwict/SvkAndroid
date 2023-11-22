package com.qwict.svkandroid.ui.viewModels.states

import com.qwict.svkandroid.common.Constants.EMPTY_USER
import com.qwict.svkandroid.domain.model.User

data class AuthUiState(

    val error: String = "",
    val user: User = EMPTY_USER,
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val isRequestSendDialogVisible: Boolean = false,
)
