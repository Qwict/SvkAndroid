package com.qwict.svkandroid.domain.validator

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String = "",
)
