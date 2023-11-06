package com.qwict.svkandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.viewModels.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun ScanScreen(viewModel: MainViewModel, nextNav: () -> Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val scanner = BarcodeScanner(context, BarcodeFormat.ITF) { barcode ->
        viewModel.currentBarcode.value = barcode
        nextNav()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Scan Screen", color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                scope.launch {
                    scanner.startScan()
                }
            },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
        ) {
            Text(text = stringResource(R.string.btn_scan_barcode), fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { nextNav() },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
        ) {
            Text(text = stringResource(R.string.btn_enter_manually), fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
