package com.qwict.svkandroid.ui.viewModels.states

import com.qwict.svkandroid.data.remote.dto.UserDto

data class SvkAndroidUiState(
    var user: UserDto = UserDto(),
)
data class LoginState(
    var email: String = "",
    var password: String = "",
)
