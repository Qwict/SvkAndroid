package com.qwict.svkandroid.data

import com.qwict.svkandroid.dto.User

data class SvkAndroidUiState(
    var user: User = User(),
)
data class LoginState(
    var email: String = "",
    var password: String = ""

)
