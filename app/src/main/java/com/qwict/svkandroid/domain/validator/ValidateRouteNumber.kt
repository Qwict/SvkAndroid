package com.qwict.svkandroid.domain.validator

class ValidateRouteNumber {
    operator fun invoke(routeNumber: String): ValidationResult {
        return if (routeNumber.isBlank()) {
            ValidationResult(
                successful = false,
                errorMessage = "Route number cannot be empty",
            )
        } else {
            ValidationResult(
                successful = true,
            )
        }
    }
}
