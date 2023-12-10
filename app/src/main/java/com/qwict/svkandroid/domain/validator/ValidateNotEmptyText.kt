package com.qwict.svkandroid.domain.validator

import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.stringRes.ResourceProvider

class ValidateNotEmptyText(private val resourceProvider: ResourceProvider) {
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
