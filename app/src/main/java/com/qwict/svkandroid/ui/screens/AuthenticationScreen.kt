package com.qwict.svkandroid.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.MainViewModel
import com.qwict.svkandroid.ui.components.IndeterminateCircularIndicator
import com.qwict.svkandroid.ui.theme.SvkAndroidTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: this is an implementation for auth0, we will probably need to change this to our own implementation
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun AuthenticationScreen(
    viewModel: MainViewModel,
    userAuthNav: () -> Unit,
    loginNav: () -> Unit,
) {

    if (viewModel.userIsAuthenticated) {
        userAuthNav()
    } else {
        loginNav()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.svk_logo_zonder_slogan),
            contentDescription = "Loading"
        )
//        Spacer(modifier = Modifier.height(32.dp))
//        IndeterminateCircularIndicator()
    }
}

@Preview(showSystemUi = true)
@Composable
fun AuthenticationScreenPreview() {
    SvkAndroidTheme(darkTheme = false) {
        AuthenticationScreen(
            viewModel = MainViewModel(),
            userAuthNav = {},
            loginNav = {},
        )
    }
}
