package com.qwict.svkandroid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qwict.svkandroid.ui.screens.EditScreen
import com.qwict.svkandroid.ui.screens.PermissionScreen
import com.qwict.svkandroid.ui.screens.PhotoScreen
import com.qwict.svkandroid.ui.screens.RouteEditScreen
import com.qwict.svkandroid.ui.screens.RouteScreen
import com.qwict.svkandroid.ui.screens.ScanScreen
import com.qwict.svkandroid.ui.screens.UploadScreen
import com.qwict.svkandroid.ui.viewModels.MainViewModel

@Composable
fun NavGraph(navController: NavHostController, viewModel: MainViewModel = hiltViewModel()) {
    NavHost(navController = navController, startDestination = Navigations.Permission.route) {
        composable(route = Navigations.Permission.route) {
            PermissionScreen(nextNav = {
                navController.navigate(Navigations.RouteSelect.route) {
                    popUpTo(Navigations.Permission.route) { inclusive = true }
                }
            })
        }

        composable(route = Navigations.RouteSelect.route) {
            RouteScreen(viewModel) {
                navController.navigate(Navigations.RouteEdit.route)
            }
        }

        composable(
            route = Navigations.RouteEdit.route,
        ) {
            RouteEditScreen({ navController.navigate(Navigations.Scan.route) }, {
                navController.navigate(
                    Navigations.Photo.route,
                )
            }, viewModel)
        }

        composable(Navigations.Scan.route) {
            ScanScreen(viewModel) { navController.navigate(Navigations.Edit.route) }
        }
        composable(
            route = Navigations.Edit.route,
        ) {
            EditScreen({
                navController.navigate(Navigations.RouteEdit.route) {
                    popUpTo(Navigations.RouteEdit.route) { inclusive = true }
                }
            }, viewModel)
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