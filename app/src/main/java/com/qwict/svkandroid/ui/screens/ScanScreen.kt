package com.qwict.svkandroid.ui.screens

import android.content.Context
import android.util.Log
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
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun ScanScreen(viewModel: MainViewModel, nextNav: () -> Unit) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val scanner = BarcodeScanner(context) { ->
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
                    scanner.startScan(viewModel = viewModel)
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

private class BarcodeScanner(appContext: Context, val nextNav: () -> Unit) {

    private val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_ITF,
        ).enableAutoZoom()
        .build()

    private val scanner = GmsBarcodeScanning.getClient(appContext, options)

    fun startScan(viewModel: MainViewModel) {
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                // Task completed successfully
                if (barcode.rawValue != null) {
                    viewModel.currentBarcode.value = barcode.rawValue!!
                    Log.i("BarcodeScanner", "Barcode: ${viewModel.currentBarcode.value}")
                    nextNav()
                } else {
                    Log.e("BarcodeScanner", "Barcode is null")
//                    TODO: add a snackbar
                }
                nextNav()
            }
            .addOnCanceledListener {
                // Task canceled
            }
            .addOnFailureListener {
                // Task failed with an exception
            }
    }
}
