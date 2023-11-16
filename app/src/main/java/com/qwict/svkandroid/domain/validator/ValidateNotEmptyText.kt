package com.qwict.svkandroid.domain.validator

class ValidateNotEmptyText {
    operator fun invoke(text: String, name: String): ValidationResult {
        return if (text.isBlank()) {
            ValidationResult(
                successful = false,
                errorMessage = "$name must be filled in",
            )
        } else {
            ValidationResult(
                successful = true,
            )
        }
    }
}
