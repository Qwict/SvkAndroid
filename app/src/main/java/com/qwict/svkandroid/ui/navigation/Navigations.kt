package com.qwict.svkandroid.ui.navigation

import androidx.annotation.StringRes
import com.qwict.svkandroid.R

/**
 * Enumeration class representing different screens and their corresponding routes in the application.
 *
 * @property route The route associated with the screen.
 * @property title The resource ID of the screen title.
 */
enum class Navigations(val route: String, @StringRes val title: Int) {
    AuthenticationRoute(
        route = "Authentication",
        title = R.string.authentication_route,
    ),
    Authenticate(
        route = "Authenticate",
        title = R.string.logged_out_title,
    ),
    Register(
        route = "Register",
        title = R.string.register_route,
    ),
    MainRoute(
        route = "Main",
        title = R.string.main_route,
    ),
    Permission(
        route = "Permission",
        title = R.string.camera_permission,
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
//        route = "RouteEdit/{selectedRouteNr}",
        route = "RouteEdit",
        title = R.string.title_overview_screen,
    ),
    RouteSelect(
        route = "Route",
        title = R.string.app_name,
    ),
}
