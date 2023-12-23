package com.qwict.svkandroid.domain.validator

import android.util.Patterns
import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.stringRes.ResourceProvider

/**
 * Validator class for email addresses.
 *
 * @param resourceProvider The resource provider for accessing application resources.
 */
class ValidateEmail(private val resourceProvider: ResourceProvider) {
    /**
     * Validates the provided email address.
     *
     * @param email The email address to be validated.
     * @return A [ValidationResult] object indicating the success or failure of the validation.
     */
    operator fun invoke(email: String): ValidationResult {
        return if (email.isEmpty()) {
            ValidationResult(
                successful = false,
                errorMessage = resourceProvider.getString(R.string.email_cannot_be_empty_err),
            )
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ValidationResult(
                successful = false,
                errorMessage = resourceProvider.getString(R.string.email_is_not_valid_err),
            )
        } else {
            ValidationResult(
                successful = true,
            )
        }
    }
}
