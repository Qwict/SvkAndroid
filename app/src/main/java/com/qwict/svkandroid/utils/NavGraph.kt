package com.qwict.svkandroid.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.qwict.svkandroid.ui.MainViewModel
import com.qwict.svkandroid.ui.screens.AuthenticationScreen
import com.qwict.svkandroid.ui.screens.EditScreen
import com.qwict.svkandroid.ui.screens.PhotoScreen
import com.qwict.svkandroid.ui.screens.RouteEditScreen
import com.qwict.svkandroid.ui.screens.RouteScreen
import com.qwict.svkandroid.ui.screens.ScanScreen
import com.qwict.svkandroid.ui.screens.UploadScreen

@Composable
fun NavGraph(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = getStartDestination(viewModel)) {
        composable(Navigations.Authenticate.route) {
            AuthenticationScreen(
                viewModel = viewModel,
                userAuthenticatedNav = {
                    navController.navigate(Navigations.RouteSelect.route) {
                        popUpTo(Navigations.Authenticate.route) { inclusive = true }
                    }
                },
            )
        }

        composable(route = Navigations.RouteSelect.route) {
            RouteScreen(nextNav = {
                navController.navigate(Navigations.RouteEdit.route)
            })
        }

        composable(
            route = Navigations.RouteEdit.route,
        ) {
            RouteEditScreen({ navController.navigate(Navigations.Scan.route) }, { navController.navigate(Navigations.Photo.route) })
        }

        composable(Navigations.Scan.route) {
            ScanScreen { navController.navigate(Navigations.Edit.route.plus("/$it")) }
        }
        composable(
            route = Navigations.Edit.route.plus("/{barcode_value}"),
            arguments = listOf(
                navArgument("barcode_value") {
                    type =
                        NavType.StringType
                },
            ),
        ) {
            EditScreen(it.arguments?.getString("barcode_value")) {
                navController.navigate(Navigations.RouteEdit.route) {
                    popUpTo(Navigations.RouteEdit.route) { inclusive = true }
                }
            }
        }
        composable(Navigations.Photo.route) {
            PhotoScreen { navController.navigate(Navigations.Upload.route) }
        }
        composable(Navigations.Upload.route) {
            UploadScreen {
                navController.navigate(Navigations.RouteEdit.route) {
                    popUpTo(Navigations.RouteEdit.route) { inclusive = true }
                }
            }
        }
    }
}

private fun getStartDestination(viewModel: MainViewModel): String {
    return if (viewModel.userIsAuthenticated) {
        Navigations.RouteSelect.route
    } else {
        Navigations.Authenticate.route
    }
}
