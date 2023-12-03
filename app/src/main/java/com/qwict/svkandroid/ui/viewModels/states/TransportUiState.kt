package com.qwict.svkandroid.ui.viewModels.states

import android.graphics.Bitmap
import android.net.Uri
import java.util.UUID

data class TransportUiState(
    val routeNumber: String = "",

    val routeNumberError: String = "",

    val licensePlate: String = "",
    val licensePlateError: String = "",
    val driverName: String = "",
    val driverNameError: String = "",

    val selectedImage: Bitmap? = null,

    val newCargoNumber: String = "",
    val originalCargoNumber: String = "",

    val cargoNumberError: String = "",
    val cargoNumbers: List<String> = listOf(),
    val cargoNumbersError: String = "",

    val images: Map<UUID, Bitmap> = emptyMap(),
    val imagesError: String = "",
    val imageUris: Map<UUID, Uri> = emptyMap(),

    val isLoading: Boolean = false,
    val error: String = "",
)
