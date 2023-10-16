package com.qwict.svkandroid.utils

import androidx.annotation.StringRes
import com.qwict.svkandroid.R

enum class Navigations(val route: String, @StringRes val title: Int) {
    Start(
        route = "Start",
        title = R.string.initial_title,
    ),
    Scan(
        route = "Scan",
        title = R.string.logged_in_title,
    ),
    Login(
        route = "Login",
        title = R.string.logged_out_title,
    ),
    Edit(
        route = "Edit",
        title = R.string.title_edit_screen,
    ),
    Photo(
        route = "Photo",
        title = R.string.app_name,
    ),
    Upload(
        route = "Upload",
        title = R.string.app_name,
    ),
}
