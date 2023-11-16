package com.qwict.svkandroid.domain.validator

class ValidatePassword {
    operator fun invoke(password: String): ValidationResult {
        return if (password.isBlank()) {
            ValidationResult(
                successful = false,
                errorMessage = "Password cannot be empty",
            )
        } else {
            ValidationResult(
                successful = true,
            )
        }
    }
}
