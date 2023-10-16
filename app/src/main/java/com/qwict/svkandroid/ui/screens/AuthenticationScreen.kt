package com.qwict.svkandroid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.qwict.svkandroid.ui.MainViewModel

// TODO: this is an implementation for auth0, we will probably need to change this to our own implementation
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
    Column {
        Text(text = "Authentication Screen", color = MaterialTheme.colorScheme.onSurface)
    }
}
