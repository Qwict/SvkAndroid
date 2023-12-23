package com.qwict.svkandroid.domain.validator

import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.stringRes.ResourceProvider

/**
 * Validator class for non-empty text.
 *
 * @param resourceProvider The resource provider for accessing application resources.
 */
class ValidateNotEmptyText(private val resourceProvider: ResourceProvider) {
    /**
     * Validates that a given text is not empty or blank.
     *
     * @param text The text to be validated.
     * @param name The name or description of the text for error messages.
     * @return A [ValidationResult] object indicating the success or failure of the validation.
     */
    operator fun invoke(text: String, name: String): ValidationResult {
        return if (text.isBlank()) {
            ValidationResult(
                successful = false,
                errorMessage = resourceProvider.getString(R.string.must_be_filled_in_err, name),
            )
        } else {
            ValidationResult(
                successful = true,
            )
        }
    }
}
