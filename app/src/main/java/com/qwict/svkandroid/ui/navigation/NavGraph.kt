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
import com.qwict.svkandroid.ui.screens.LoginScreen
import com.qwict.svkandroid.ui.screens.PermissionScreen
import com.qwict.svkandroid.ui.screens.PhotoScreen
import com.qwict.svkandroid.ui.screens.RegisterScreen
import com.qwict.svkandroid.ui.screens.RouteEditScreen
import com.qwict.svkandroid.ui.screens.RouteScreen
import com.qwict.svkandroid.ui.viewModels.AuthViewModel
import com.qwict.svkandroid.ui.viewModels.TransportViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
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
                LoginScreen(
                    onUpdateLoginState = { authViewModel.onUpdateLoginState(it) },
                    authFormState = authViewModel.authFormState,
                    login = { authViewModel.login() },
                    authUiState = authViewModel.authUiState,
                    switchPasswordVisibility = { authViewModel.switchPasswordVisibility() },
                    navigateToRegisterScreen = { navController.navigate(Navigations.Register.route) },
                    clearValidationErrors = { authViewModel.clearValidationErrors() },
                )
            }
            composable(route = Navigations.Register.route) {
                val authViewModel = it.sharedViewModel<AuthViewModel>(navController)
                RegisterScreen(
                    authState = authViewModel.authUiState,
                    registerUser = { authViewModel.registerUser() },
                    authFormState = authViewModel.authFormState,
                    onEvent = { authViewModel.onUpdateLoginState(it) },
                    switchPasswordVisibility = { authViewModel.switchPasswordVisibility() },
                    clearValidationErrors = { authViewModel.clearValidationErrors() },
                    navigateToLoginScreen = {
                        navController.navigate(Navigations.Authenticate.route) {
                            popUpTo(Navigations.Authenticate.route) { inclusive = true }
                        }
                    },
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
                RouteScreen(
                    isRouteNumberValid = { transportViewModel.isRouteNumberValid() },
                    onUpdateTransportState = { transportViewModel.onUpdateTransportState(it) },
                    transportUiState = transportViewModel.transportUiState,
                    navigateToRouteEditRoute = { navController.navigate(Navigations.RouteEdit.route) },
                    scanRouteNumber = { transportViewModel.scanRouteNumber() },
                )
            }
            composable(route = Navigations.RouteEdit.route) {
                val transportViewModel = it.sharedViewModel<TransportViewModel>(navController)
                RouteEditScreen(
                    onUpdateTransportState = { transportViewModel.onUpdateTransportState(it) },
                    navigateToPhotoRoute = { navController.navigate(Navigations.Photo.route) },
                    deleteImageOnIndex = { transportViewModel.deleteImageOnIndex(it) },
                    toggleShowDialogState = { transportViewModel.toggleShowDialogState(it) },
                    showDialogState = transportViewModel.showDialogState,
                    selectedImage = transportViewModel.selectedImage,
                    transportUiState = transportViewModel.transportUiState,
                    scanCargoNumber = { transportViewModel.scanCargoNumber() },
                    isCargoNumberValidThenSave = { transportViewModel.isCargoNumberValidThenSave() },
                    isDriverNameLicensePlateValid = { transportViewModel.isDriverNameLicensePlateValid() },

                    clearCargoNumberError = { transportViewModel.clearCargoNumberError() },
                    startEditingCargoNumber = { transportViewModel.startEditingCargoNumber() },
                    stopEditingCargoNumber = { transportViewModel.stopEditingCargoNumber() },

                    finishTransport = { transportViewModel.finishTransport() },
                    navigateToRouteScreen = {
                        navController.navigate(Navigations.RouteSelect.route) {
                            popUpTo(Navigations.RouteEdit.route) { inclusive = true }
                        }
                    },
                    isTransportValid = { transportViewModel.isDriverNameLicensePlateValid() }
                )
            }
            composable(route = Navigations.Photo.route) {
                val transportViewModel = it.sharedViewModel<TransportViewModel>(navController)
                PhotoScreen(
                    onTakePhoto = { transportViewModel.onTakePhoto(it) },
                    capturedImages = transportViewModel.transportUiState.images.values.toList(),
                    goBack = { navController.navigateUp() },
                )
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
