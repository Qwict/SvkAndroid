package com.qwict.svkandroid.domain.validator

import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.stringRes.ResourceProvider

/**
 * Validator class for non-empty lists.
 *
 * @param resourceProvider The resource provider for accessing application resources.
 */
class ValidateNotEmptyList(private val resourceProvider: ResourceProvider) {
    /**
     * Validates that a given list is not empty.
     *
     * @param list The list to be validated.
     * @param name The name or description of the list for error messages.
     * @return A [ValidationResult] object indicating the success or failure of the validation.
     */
    operator fun invoke(
        list: List<Any>,
        name: String,
    ): ValidationResult {
        return if (list.isEmpty()) {
            ValidationResult(
                successful = false,
                errorMessage = resourceProvider.getString(R.string.list_cannot_be_empty_err, name),
            )
        } else {
            ValidationResult(
                successful = true,
            )
        }
    }
}
