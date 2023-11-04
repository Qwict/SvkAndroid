package com.qwict.svkandroid.ui.screens

import android.content.Context
import android.util.Log
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

enum class BarcodeFormat(val format: Int) {
    ITF(Barcode.FORMAT_ITF),
    QR(Barcode.FORMAT_QR_CODE),
}
internal class BarcodeScanner(
    appContext: Context,
    barcodeFormat: BarcodeFormat,
    private val onBarcodeScanned: (String) -> Unit,
) {

    private val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(barcodeFormat.format)
        .enableAutoZoom()
        .build()

    private val scanner = GmsBarcodeScanning.getClient(appContext, options)

    fun startScan() {
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                // Task completed successfully
                if (barcode.rawValue != null) {
                    onBarcodeScanned(barcode.rawValue!!)
                    Log.i("BarcodeScanner", "Barcode: ${barcode.rawValue}")
                } else {
                    Log.e("BarcodeScanner", "Barcode is null")
                    // TODO: add a snackbar
                }
            }
            .addOnCanceledListener {
                // Task canceled
            }
            .addOnFailureListener {
                // Task failed with an exception
            }
    }
}
