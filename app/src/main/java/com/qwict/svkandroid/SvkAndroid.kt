package com.qwict.svkandroid

import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.qwict.svkandroid.data.local.RoomContainer
import com.qwict.svkandroid.data.local.RoomContainerImpl
import com.qwict.svkandroid.ui.components.Loading
import com.qwict.svkandroid.ui.components.SvkAndroidAppbar
import com.qwict.svkandroid.ui.navigation.NavGraph
import com.qwict.svkandroid.ui.navigation.Navigations
import com.qwict.svkandroid.ui.screens.AuthenticationScreen
import com.qwict.svkandroid.ui.viewModels.AuthViewModel
import com.qwict.svkandroid.ui.viewModels.states.AuthState
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class SvkAndroidApplication : Application() {
    private lateinit var appScope: CoroutineScope
    private lateinit var container: RoomContainer
    override fun onCreate() {
        super.onCreate()
        appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        container = RoomContainerImpl(this, appScope)
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}

@Composable
fun SvkAndroidApp(
    viewModel: AuthViewModel = hiltViewModel(),
) {
    when (viewModel.authUiState.value.authState) {
        is AuthState.LoggedIn -> AppView(viewModel = viewModel)
        is AuthState.Idle -> AuthView(viewModel = viewModel)
        is AuthState.Loading -> Loading()
    }
}

@Composable
fun AuthView(message: String = "", viewModel: AuthViewModel) {
    AuthenticationScreen(authViewModel = viewModel, message = message)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppView(
    navController: NavHostController = rememberNavController(),
    viewModel: AuthViewModel,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = Navigations.values().find { it.route == backStackEntry?.destination?.route }
        ?: Navigations.Permission

    Scaffold(
        topBar = {
            SvkAndroidAppbar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() },
                onLogOutClicked = {
                    viewModel.logout()
                },
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(navController = navController)
        }
    }
}
