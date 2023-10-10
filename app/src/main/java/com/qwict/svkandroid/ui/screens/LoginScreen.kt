package com.qwict.svkandroid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.qwict.svkandroid.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    onLoginButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var email = ""
    var password = ""
    val mask = '*'
    Column() {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
//            TODO: Make this work
//            visualTransformation: PasswordVisualTransformation(mask: Char)
//            VisualTransformation = PasswordVisualTransformation(mask: Char)
        )
        OutlinedButton(onClick = { onLoginButtonClicked() }) {
            Text(text = "Login")
        }
    }
}
//    }
// }
