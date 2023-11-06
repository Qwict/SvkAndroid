package com.qwict.svkandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qwict.svkandroid.ui.theme.SvkAndroidTheme
import com.qwict.svkandroid.ui.viewModels.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(viewModel: MainViewModel, nextNav: () -> Unit) {
    var text by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Route Screen",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineLarge,
        )
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Route") },
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.LocalShipping,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
        )
        Button(onClick = { nextNav() }) {
            Text(text = "Select")
        }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val scanner = BarcodeScanner(context, BarcodeFormat.QR) { barcode ->
            viewModel.currentQrcode.value = barcode
            nextNav()
        }
        Button(onClick = {
            scope.launch {
                scanner.startScan()
            }
        }) {
            Text("Scan QR-code")
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RouteScreenPreview() {
    SvkAndroidTheme(darkTheme = false) {
        RouteScreen(MainViewModel(), {})
    }
}

@Preview(showSystemUi = true)
@Composable
fun RouteDarkScreenPreview() {
    SvkAndroidTheme(darkTheme = true) {
        RouteScreen(MainViewModel(), {})
    }
}
