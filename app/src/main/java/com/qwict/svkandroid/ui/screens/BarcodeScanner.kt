package com.qwict.svkandroid.ui.screens

import android.content.Context
import android.util.Log
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

/**
 * Enum class representing different barcode formats for scanning.
 *
 * @property format The barcode format integer value.
 */
enum class BarcodeFormat(val format: Int) {
    ITF(Barcode.FORMAT_ITF),
    QR(Barcode.FORMAT_QR_CODE),
}

/**
 * Internal class responsible for scanning barcodes using Google's Mobile Vision API.
 *
 * @property appContext The application context.
 * @property barcodeFormat The desired barcode format to scan.
 * @property onBarcodeScanned Callback function to handle the scanned barcode result.
 */
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

    /**
     * Initiates the barcode scanning process.
     *
     * Starts the barcode scanner and handles the success, cancellation, and failure events.
     */
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
