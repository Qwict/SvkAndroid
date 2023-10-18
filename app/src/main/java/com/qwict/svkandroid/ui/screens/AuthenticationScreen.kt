package com.qwict.svkandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun AuthenticationScreen(
    modifier: Modifier = Modifier,
    userAuthNav: () -> Unit,
    loginNav: () -> Unit,
    viewModel: MainViewModel,
) {
    Column() {
        AuthenticationView(modifier, viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
) {
    val scope = rememberCoroutineScope()
    val email = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val Title = if (viewModel.userIsAuthenticated) {
            stringResource(R.string.logged_in_title)
        } else {
            if (viewModel.appJustLaunched) {
                stringResource(R.string.initial_title)
            } else {
                stringResource(R.string.logged_out_title)
            }
        }
        Title(
            text = Title,
        )

        if (viewModel.userIsAuthenticated) {
            UserInfoRow(
                label = stringResource(R.string.email_label),
                value = viewModel.user.email,
            )
            //            UserPicture(
            //                url = viewModel.user.picture,
            //                description = viewModel.user.name,
            //            )
        } else {
            TextField(
                label = { Text(text = "Username") },
                value = email.value,
                onValueChange = { email.value = it },
            )

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Password") },
                value = password.value,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password.value = it },
            )
        }

        val buttonText: String
        val onClickAction: () -> Unit
        if (viewModel.userIsAuthenticated) {
            buttonText = stringResource(R.string.log_out_button)
            onClickAction = {
                viewModel.logout()
                scope.launch {
                    viewModel.snackbarHostState.showSnackbar("Logged out")
                }
                email.value = TextFieldValue()
                password.value = TextFieldValue()
            }
        } else {
            buttonText = stringResource(R.string.log_in_button)
            onClickAction = {
//                TODO: is returning a bool elegant enough?
                if (!viewModel.login(email, password)) {
                    scope.launch {
                        viewModel.snackbarHostState.showSnackbar(
                            message = "Failed to login",
                            withDismissAction = true,
                        )
                    }
                }
            }
        }
        LogButton(
            text = buttonText,
            onClick = onClickAction,
        )
    }
}

@Composable
fun UserInfoRow(
    label: String,
    value: String,
) {
    Row {
        Text(
            text = label,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            ),
        )
        Spacer(
            modifier = Modifier.width(10.dp),
        )
        Text(
            text = value,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontSize = 20.sp,
            ),
        )
    }
}

@Composable
fun Title(
    text: String,
) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
        ),
    )
}

@Composable
fun LogButton(
    text: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
            )
        }
    }
}
