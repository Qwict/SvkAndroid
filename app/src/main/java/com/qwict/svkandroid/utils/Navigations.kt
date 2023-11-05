package com.qwict.svkandroid.utils

import androidx.annotation.StringRes
import com.qwict.svkandroid.R

enum class Navigations(val route: String, @StringRes val title: Int) {
    Authenticate(
        route = "Authenticate",
        title = R.string.logged_out_title,
    ),

    Permission(
        route = "Permission",
        title = R.string.camera_permission,
    ),

    Scan(
        route = "Scan",
        title = R.string.title_barcode_scanner_screen,
    ),
    Edit(
        route = "Edit",
        title = R.string.title_edit_screen,
    ),
    Photo(
        route = "Photo",
        title = R.string.title_photo_screen,
    ),
    Upload(
        route = "Upload",
        title = R.string.title_uploading_screen,
    ),
    RouteEdit(
        route = "RouteEdit",
        title = R.string.title_overview_screen,
    ),
    RouteSelect(
        route = "Route",
        title = R.string.app_name,
    ),
}
