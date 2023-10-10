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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SvkAndroidAppbar(
    currentScreen: Navigations,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    onLogOutClicked: () -> Unit = {},
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
            if (currentScreen.route != Navigations.Login.route) {
                IconButton(onClick = { onLogOutClicked() }) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "The Account screen",
                    )
                }
            } else {
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
                onLogOutClicked = {
                    navController.navigate(Navigations.Login.route) {
                        popUpTo(Navigations.Scan.route) { inclusive = true }
                    }
                },
            )
        },
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(navController = navController, viewModel = viewModel)
        }
    }
}
