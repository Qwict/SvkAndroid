package com.qwict.svkandroid.domain.validator

/**
 * Data class representing the result of a validation process.
 *
 * @param successful Indicates whether the validation was successful (true) or not (false).
 * @param errorMessage A message describing the reason for validation failure, empty if successful.
 */
data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String = "",
)
