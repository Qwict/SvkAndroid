package com.qwict.svkandroid.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.qwict.svkandroid.ui.MainViewModel
import com.qwict.svkandroid.ui.screens.AuthenticationScreen
import com.qwict.svkandroid.ui.screens.EditScreen
import com.qwict.svkandroid.ui.screens.LoginScreen
import com.qwict.svkandroid.ui.screens.PhotoScreen
import com.qwict.svkandroid.ui.screens.ScanScreen
import com.qwict.svkandroid.ui.screens.UploadScreen

@Composable
fun NavGraph(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = Navigations.Start.route) {
        composable(Navigations.Start.route) {
            AuthenticationScreen(
                viewModel = viewModel,
                userAuthNav = {
                    navController.navigate(Navigations.Scan.route) {
                        popUpTo(Navigations.Start.route) { inclusive = true }
                    }
                },
                loginNav = {
                    navController.navigate(Navigations.Login.route) {
                        popUpTo(Navigations.Start.route) { inclusive = true }
                    }
                },
            )
        }
        composable(Navigations.Scan.route) {
            ScanScreen(nextNav = { navController.navigate(Navigations.Edit.route) })
        }
        composable(Navigations.Login.route) {
            LoginScreen(viewModel = viewModel, onLoginButtonClicked = {
                navController.navigate(Navigations.Scan.route) {
                    popUpTo(Navigations.Login.route) { inclusive = true }
                }
            })
        }
        composable(Navigations.Edit.route) {
            EditScreen { navController.navigate(Navigations.Photo.route) }
        }
        composable(Navigations.Photo.route) {
            PhotoScreen { navController.navigate(Navigations.Upload.route) }
        }
        composable(Navigations.Upload.route) {
            UploadScreen {
                navController.navigate(Navigations.Scan.route) {
                    popUpTo(Navigations.Scan.route) { inclusive = true }
                }
            }
        }
    }
}
