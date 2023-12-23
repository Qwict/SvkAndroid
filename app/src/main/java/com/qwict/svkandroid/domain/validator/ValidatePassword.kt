package com.qwict.svkandroid.domain.validator

import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.stringRes.ResourceProvider

/**
 * Validator class for password validation.
 *
 * @param resourceProvider The resource provider for accessing application resources.
 */
class ValidatePassword(private val resourceProvider: ResourceProvider) {
    /**
     * Validates that a given password is not empty or blank.
     *
     * @param password The password to be validated.
     * @return A [ValidationResult] object indicating the success or failure of the validation.
     */
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
