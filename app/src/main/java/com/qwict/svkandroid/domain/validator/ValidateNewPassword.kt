package com.qwict.svkandroid.domain.validator

import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.stringRes.ResourceProvider

/**
 * Validator class for new passwords.
 *
 * @param resourceProvider The resource provider for accessing application resources.
 */
class ValidateNewPassword(private val resourceProvider: ResourceProvider) {
    /**
     * Validates a new password and its confirmation.
     *
     * @param password The new password to be validated.
     * @param confirmPassword The confirmation of the new password.
     * @return A [ValidationResult] object indicating the success or failure of the validation.
     */
    operator fun invoke(password: String, confirmPassword: String): ValidationResult {
        var errorMessages = emptyList<String>()
        if (password != confirmPassword) {
            errorMessages = errorMessages + resourceProvider.getString(R.string.the_passwords_do_not_match_err)
        } else {
            if (password.length < 8) {
                errorMessages = addFirstPartIfNeeded(errorMessages, resourceProvider)
                errorMessages = errorMessages + resourceProvider.getString(R.string._8_characters_err)
            }
            if (!password.contains(Regex("[0-9]"))) {
                errorMessages = addFirstPartIfNeeded(errorMessages, resourceProvider)
                errorMessages = errorMessages + resourceProvider.getString(R.string.one_digit_err)
            }
            if (!password.contains(Regex("[a-z]"))) {
                errorMessages = addFirstPartIfNeeded(errorMessages, resourceProvider)
                errorMessages = errorMessages + resourceProvider.getString(R.string.one_lowercase_letter_err)
            }
        }

        return ValidationResult(
            successful = errorMessages.isEmpty(),
            errorMessage = errorMessages.joinToString(separator = " "),
        )
    }

    /**
     * Adds the first part of the error message if needed.
     *
     * @param errorMessages The list of error messages.
     * @param resourceProvider The resource provider for accessing application resources.
     * @return The updated list of error messages.
     */
    private fun addFirstPartIfNeeded(errorMessages: List<String>, resourceProvider: ResourceProvider): List<String> {
        if (errorMessages.isEmpty()) {
            return errorMessages + resourceProvider.getString(R.string.the_password_must_at_least_contain_err)
        }
        return errorMessages
    }
}
