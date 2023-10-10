package com.qwict.svkandroid

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.qwict.svkandroid.ui.MainViewModel
import com.qwict.svkandroid.utils.NavGraph
import com.qwict.svkandroid.utils.Navigations

// enum class SvkAndroidScreens(@StringRes val title: Int) {
//    LoginScreen(title = R.string.title_login_screen),
//    LoadingScreen(title = R.string.title_loading_screen),
//    OverviewScreen(title = R.string.title_overview_screen),
//    BarcodeScannerScreen(title = R.string.title_barcode_scanner_screen),
//    UploadingScreen(title = R.string.title_uploading_screen),
//    InformationScreen(title = R.string.title_information_screen),
// }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SvkAndroidAppbar(
    currentScreen: Navigations,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    onAccountButtonClicked: () -> Unit = {},
) {
//    val healthService = HealthService()
//    val health = healthService.getHealth()

//    First implementation to do api call, not yet working for me... Joris
//    val call = HealthService().healthApi.getHealth()
//    var version = ""
//    call.enqueue(object : Callback<HealthDto> {
//        override fun onResponse(call: Call<HealthDto>, response: Response<HealthDto>) {
//            if (response.isSuccessful) {
//                version = response.body()?.version.toString()
//            } else {
//                version = "Server is offline"
//            }
//        }
//
//        override fun onFailure(call: Call<HealthDto>, t: Throwable) {
//            version = "Server had internal error on getting version"
//        }
//    })

    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreen.title) + " " + "version") }, // Version here
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Return",
                    )
                }
            }
        },
//        TODO: Should see in mockup what we want here... (this is the top right icon)
        actions = {
            IconButton(onClick = { onAccountButtonClicked() }) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = "The Account screen",
                )
            }
        },

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SvkAndroidApp(
    viewModel: MainViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = Navigations.valueOf(
        backStackEntry?.destination?.route ?: Navigations.Login.name,
    )

    Scaffold(
        topBar = {
            SvkAndroidAppbar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                onAccountButtonClicked = {
                    navController.navigate(Navigations.Login.route)
                },
            )
        },
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(navController = navController, viewModel = viewModel)
        }

//        NavHost(
//            navController = navController,
//            startDestination = SvkAndroidScreens.LoginScreen.name,
//            modifier = Modifier.padding(innerPadding),
//        ) {
//            composable(route = SvkAndroidScreens.LoginScreen.name) {
//                LoginScreen(
//                    viewModel = viewModel,
//                    onLoginButtonClicked = {
//                        navController.navigate(SvkAndroidScreens.LoadingScreen.name)
//                    },
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(dimensionResource(R.dimen.padding_medium)),
//                )
//            }
//            composable(route = SvkAndroidScreens.LoginScreen.name) {
//                AuthenticationScreen(
//                    viewModel = viewModel,
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(dimensionResource(R.dimen.padding_medium)),
//                )
//            }
// //            composable(route = SvkAndroidScreens.Example.name) {
// //                val context = LocalContext.current
// //                ExampleScreen(
// //                    viewModel = viewModel,
// //                    onExamplButtonClicked = {
// //                        navController.navigate(SvkAndroidScreens.OtherScreen.name)
// //                    },
// //                    modifier = Modifier.fillMaxHeight()
// //                )
// //            }
//        }
    }
}
