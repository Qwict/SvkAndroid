package com.qwict.svkandroid.utils

import androidx.annotation.StringRes
import com.qwict.svkandroid.R

enum class Navigations(val route: String, @StringRes val title: Int) {
    Authenticate(
        route = "Authenticate",
        title = R.string.logged_out_title,
    ),
    Start(
        route = "Start",
        title = R.string.initial_title,
    ),

    Scan(
        route = "Scan",
        title = R.string.app_name,
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
    RouteEdit(
        route = "RouteEdit",
        title = R.string.app_name,
    ),
    RouteSelect(
        route = "Route",
        title = R.string.app_name,
    ),
}
