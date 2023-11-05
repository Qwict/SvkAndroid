package com.qwict.svkandroid

import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.qwict.svkandroid.ui.components.Loading
import com.qwict.svkandroid.ui.components.SvkAndroidAppbar
import com.qwict.svkandroid.ui.screens.AuthenticationScreen
import com.qwict.svkandroid.ui.viewModels.AuthState
import com.qwict.svkandroid.ui.viewModels.AuthViewModel
import com.qwict.svkandroid.utils.NavGraph
import com.qwict.svkandroid.utils.Navigations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class SvkAndroidApplication : Application() {
    //    lateinit var userSettings: UserSettings
    private lateinit var appScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
//        userSettings = UserSettings(dataStore)
        appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
    }
}

@Composable
fun SvkAndroidApp(
    viewModel: AuthViewModel = viewModel(),
) {
    when (viewModel.authState) {
        is AuthState.LoggedIn -> AppView(viewModel = viewModel)
        is AuthState.Idle -> AuthView(viewModel = viewModel)
        is AuthState.Loading -> Loading()
        is AuthState.Error -> AuthView((viewModel.authState as AuthState.Error).message, viewModel = viewModel)
    }
}

@Composable
fun AuthView(message: String = "", viewModel: AuthViewModel) {
    AuthenticationScreen(viewModel = viewModel, message = message)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppView(
    navController: NavHostController = rememberNavController(), viewModel: AuthViewModel
) {
    Scaffold(
        topBar = {
            SvkAndroidAppbar(
                currentScreen = Navigations.Permission,
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