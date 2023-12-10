package com.qwict.svkandroid.domain.validator

import android.content.Context
import com.qwict.svkandroid.common.stringRes.AndroidResourceProvider

class Validators(context: Context) {
    val emailValidator = ValidateEmail(AndroidResourceProvider(context))
    val passwordValidator = ValidatePassword(AndroidResourceProvider(context))
    val validateNotEmptyText = ValidateNotEmptyText(AndroidResourceProvider(context))
    val validateNotEmptyList = ValidateNotEmptyList(AndroidResourceProvider(context))
    val validateNewPassword = ValidateNewPassword(AndroidResourceProvider(context))
}
