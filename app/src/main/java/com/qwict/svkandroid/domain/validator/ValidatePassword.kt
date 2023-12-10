package com.qwict.svkandroid.domain.validator

import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.stringRes.ResourceProvider

class ValidatePassword(private val resourceProvider: ResourceProvider) {
    operator fun invoke(password: String): ValidationResult {
        return if (password.isBlank()) {
            ValidationResult(
                successful = false,
                errorMessage = resourceProvider.getString(R.string.password_cannot_be_empty_err),
            )
        } else {
            ValidationResult(
                successful = true,
            )
        }
    }
}
