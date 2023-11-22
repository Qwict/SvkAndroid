package com.qwict.svkandroid.ui.viewModels.states

data class AuthFormState(
    var email: String = "",
    val emailError: String = "",

    var password: String = "",
    val passwordError: String = "",

    var passwordConfirm: String = "",
    val passwordConfirmError: String = "",

)
