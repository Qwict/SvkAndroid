package com.qwict.svkandroid.domain.validator

import android.util.Patterns
import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.stringRes.ResourceProvider

class ValidateEmail(private val resourceProvider: ResourceProvider) {
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
