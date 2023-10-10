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
            AuthenticationScreen(viewModel = viewModel)
        }
        composable(Navigations.Scan.route) {
            ScanScreen()
        }
        composable(Navigations.Login.route) {
            LoginScreen(viewModel = viewModel)
        }
        composable(Navigations.Edit.route) {
            EditScreen()
        }
        composable(Navigations.Photo.route) {
            PhotoScreen()
        }
        composable(Navigations.Upload.route) {
            UploadScreen()
        }
    }
}
