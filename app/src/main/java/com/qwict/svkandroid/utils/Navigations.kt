package com.qwict.svkandroid.utils

import androidx.annotation.StringRes

enum class Navigations(val route: String, @StringRes val title: Int) {
    Start(
        route = "start",
        title = 0
    ),
    Login(
        route = "login",
        title = 0
    ),
    Edit(
        route = "edit",
        title = 0
    ),
    Photo(
        route = "photo",
        title = 0
    ),
    Upload(
        route = "upload",
        title = 0
    )
}