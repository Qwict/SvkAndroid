package com.qwict.svkandroid.domain.validator

import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.stringRes.ResourceProvider

class ValidateNotEmptyList(private val resourceProvider: ResourceProvider) {
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
