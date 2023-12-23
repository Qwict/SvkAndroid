package com.qwict.svkandroid.domain.validator

import android.content.Context
import com.qwict.svkandroid.common.stringRes.AndroidResourceProvider

/**
 * A utility class that provides instances of various validation classes.
 *
 * @property context The Android context used for creating AndroidResourceProvider instances.
 * @property emailValidator An instance of [ValidateEmail] for email validation.
 * @property passwordValidator An instance of [ValidatePassword] for password validation.
 * @property validateNotEmptyText An instance of [ValidateNotEmptyText] for non-empty text validation.
 * @property validateNotEmptyList An instance of [ValidateNotEmptyList] for non-empty list validation.
 * @property validateNewPassword An instance of [ValidateNewPassword] for validating new passwords.
 */
class Validators(context: Context) {
    val emailValidator = ValidateEmail(AndroidResourceProvider(context))
    val passwordValidator = ValidatePassword(AndroidResourceProvider(context))
    val validateNotEmptyText = ValidateNotEmptyText(AndroidResourceProvider(context))
    val validateNotEmptyList = ValidateNotEmptyList(AndroidResourceProvider(context))
    val validateNewPassword = ValidateNewPassword(AndroidResourceProvider(context))
}
