package com.qwict.svkandroid.ui.viewModels.states

import android.graphics.Bitmap

data class TransportUiState(
    val routeNumber: String = "",
    val routeNumberError: String = "",

    val licensePlate: String = "",
    val licensePlateError: String = "",
    val driverName: String = "",
    val driverNameError: String = "",

    val newCargoNumber: String = "",
    val originalCargoNumber: String = "",
    val isEditingCargoNumber: Boolean = false,
    val cargoNumberError: String = "",
    val cargoNumbers: List<String> = listOf(),

   /* val images: List<Int> = listOf(
        R.drawable.transport_two,
        R.drawable.transport_three,
        R.drawable.transport_four,
    ),*/

    val images: List<Bitmap> = emptyList(),

    val isLoading: Boolean = false,
    val error: String = "",
)
