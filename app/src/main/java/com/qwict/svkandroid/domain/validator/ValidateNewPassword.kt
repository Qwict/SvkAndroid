package com.qwict.svkandroid.domain.validator

class ValidateNewPassword {
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        var errorMessages = emptyList<String>()
        if (password != confirmPassword) {
            errorMessages = errorMessages + "The passwords do not match"
        } else {
            if (password.length < 8) {
                errorMessages = addFirstPartIfNeeded(errorMessages)
                errorMessages = errorMessages + "8 characters"
            }
            if (!password.contains(Regex("[0-9]"))) {
                errorMessages = addFirstPartIfNeeded(errorMessages)
                errorMessages = errorMessages + "one digit"
            }
            if (!password.contains(Regex("[a-z]"))) {
                errorMessages = addFirstPartIfNeeded(errorMessages)
                errorMessages = errorMessages + "one lowercase letter"
            }
        }

        return ValidationResult(
            successful = errorMessages.isEmpty(),
            errorMessage = errorMessages.joinToString(separator = " "),
        )
    }

    private fun addFirstPartIfNeeded(errorMessages: List<String>): List<String> {
        if (errorMessages.isEmpty()) {
            return errorMessages + "The password must at least contain:"
        }
        return errorMessages
    }
}
