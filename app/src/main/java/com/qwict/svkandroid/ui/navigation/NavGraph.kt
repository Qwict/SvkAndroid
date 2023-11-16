package com.qwict.svkandroid.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.qwict.svkandroid.common.AuthenticationSingleton.isUserAuthenticated
import com.qwict.svkandroid.ui.screens.AuthenticationScreen
import com.qwict.svkandroid.ui.screens.EditScreen
import com.qwict.svkandroid.ui.screens.PermissionScreen
import com.qwict.svkandroid.ui.screens.PhotoScreen
import com.qwict.svkandroid.ui.screens.RouteEditScreen
import com.qwict.svkandroid.ui.screens.RouteScreen
import com.qwict.svkandroid.ui.screens.ScanScreen
import com.qwict.svkandroid.ui.screens.UploadScreen
import com.qwict.svkandroid.ui.viewModels.AuthViewModel
import com.qwict.svkandroid.ui.viewModels.MainViewModel
import com.qwict.svkandroid.ui.viewModels.TransportViewModel

@Composable
fun NavGraph(navController: NavHostController, viewModel: MainViewModel = hiltViewModel()) {
    NavHost(
        navController = navController,
//        startDestination = Navigations.Permission.route
        startDestination = if (isUserAuthenticated) {
            Navigations.MainRoute.route
        } else {
            Navigations.AuthenticationRoute.route
        },
    ) {
        navigation(
            startDestination = Navigations.Authenticate.route,
            route = Navigations.AuthenticationRoute.route,
        ) {
            composable(route = Navigations.Authenticate.route) {
                val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
                AuthenticationScreen(
                    onUpdateLoginState = { authViewModel.onUpdateLoginState(it) },
                    loginUiState = authViewModel.loginUiState,
                    login = { authViewModel.login() },
                    authUiState = authViewModel.authUiState,
                )
            }
        }

        navigation(
            startDestination = Navigations.Permission.route,
            route = Navigations.MainRoute.route,
        ) {
            composable(route = Navigations.Permission.route) {
                val transportViewModel = it.sharedViewModel<TransportViewModel>(navController)
                PermissionScreen(nextNav = {
                    navController.navigate(Navigations.RouteSelect.route) {
                        popUpTo(Navigations.Permission.route) { inclusive = true }
                    }
                })
            }

            composable(route = Navigations.RouteSelect.route) {
                val transportViewModel = it.sharedViewModel<TransportViewModel>(navController)
                RouteScreen(viewModel) {
                    navController.navigate(Navigations.RouteEdit.route)
                }
            }
            composable(
                route = Navigations.RouteEdit.route,
            ) {
                val transportViewModel = it.sharedViewModel<TransportViewModel>(navController)
                RouteEditScreen({ navController.navigate(Navigations.Scan.route) }, {
                    navController.navigate(
                        Navigations.Photo.route,
                    )
                }, viewModel)
            }

            composable(Navigations.Scan.route) {
                val transportViewModel = it.sharedViewModel<TransportViewModel>(navController)
                ScanScreen(viewModel) { navController.navigate(Navigations.Edit.route) }
            }
            composable(route = Navigations.Edit.route) {
                val transportViewModel = it.sharedViewModel<TransportViewModel>(navController)
                EditScreen({
                    navController.navigate(Navigations.RouteEdit.route) {
                        popUpTo(Navigations.RouteEdit.route) { inclusive = true }
                    }
                }, viewModel)
            }
            composable(Navigations.Photo.route) {
                val transportViewModel = it.sharedViewModel<TransportViewModel>(navController)
                PhotoScreen { navController.navigate(Navigations.Upload.route) }
            }
            composable(Navigations.Upload.route) {
                val transportViewModel = it.sharedViewModel<TransportViewModel>(navController)
                UploadScreen {
                    navController.navigate(Navigations.RouteEdit.route) {
                        popUpTo(Navigations.RouteEdit.route) { inclusive = true }
                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}
