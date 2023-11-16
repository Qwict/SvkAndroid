package com.qwict.svkandroid.ui.navigation

import androidx.annotation.StringRes
import com.qwict.svkandroid.R

enum class Navigations(val route: String, @StringRes val title: Int) {
    AuthenticationRoute(
        route = "Authentication",
        title = R.string.authentication_route,
    ),
    Authenticate(
        route = "Authenticate",
        title = R.string.logged_out_title,
    ),

    MainRoute(
        route = "Main",
        title = R.string.main_route,
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
