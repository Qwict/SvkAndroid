package com.qwict.svkandroid.domain.validator

class ValidateNotEmptyList {
    operator fun invoke(
        list: List<Any>,
        name: String,
    ): ValidationResult {
        return if (list.isEmpty()) {
            ValidationResult(
                successful = false,
                errorMessage = "$name cannot be empty",
            )
        } else {
            ValidationResult(
                successful = true,
            )
        }
    }
}
