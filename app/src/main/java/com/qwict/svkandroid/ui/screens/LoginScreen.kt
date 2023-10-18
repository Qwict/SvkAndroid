package com.qwict.svkandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qwict.svkandroid.ui.MainViewModel
import com.qwict.svkandroid.ui.theme.SVKTextfield

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    onLoginButtonClicked: () -> Unit = {},
) {
    var email = ""
    var password = ""
    val mask = '*'
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp, alignment = Alignment.CenterVertically),
    ) {

        SVKTextfield {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
            )
        }
        SVKTextfield {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
//            TODO: Make this work
//            visualTransformation: PasswordVisualTransformation(mask: Char)
//            VisualTransformation = PasswordVisualTransformation(mask: Char)
            )
        }
        Button(onClick = { onLoginButtonClicked() }) {
            Text(text = "Login")
        }
    }
}
//    }
// }
