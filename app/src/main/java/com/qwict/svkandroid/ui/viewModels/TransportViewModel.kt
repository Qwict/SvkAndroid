package com.qwict.svkandroid.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.qwict.svkandroid.SvkAndroidApplication
import com.qwict.svkandroid.domain.validator.Validators
import com.qwict.svkandroid.ui.screens.BarcodeFormat
import com.qwict.svkandroid.ui.screens.BarcodeScanner
import androidx.lifecycle.viewModelScope
import com.qwict.svkandroid.R
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.domain.use_cases.SelectRouteUseCase
import com.qwict.svkandroid.ui.viewModels.states.TransportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TransportViewModel @Inject constructor(
    private val validators: Validators,
) : ViewModel() {
    var transportUiState by mutableStateOf(TransportUiState())
        private set

class TransportViewModel @Inject constructor(
    private val selectRouteUseCase: SelectRouteUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TransportUiState())

    private var routeNumber by mutableStateOf(0)
    var showDialogState by mutableStateOf(false)
        private set
    var selectedImage by mutableStateOf(0)
        private set
    var transportUiState by mutableStateOf(TransportUiState())
        private set

    fun deleteImageOnIndex(imageIndex: Int) {
        Log.i("TransportViewModel", "deleteImageOnIndex: $imageIndex")
        if (imageIndex >= 0 && imageIndex < transportUiState.images.size) {
            transportUiState = transportUiState.copy(
                images = transportUiState.images.toMutableList().apply { removeAt(imageIndex) },
            )
            Log.i("TransportViewModel", "deleteImageOnIndex: $imageIndex")
        } else {
            // Handle index out of bounds, e.g., throw an exception or log an error
            println("Invalid index: $imageIndex")
        }
    }

    fun toggleShowDialogState(imageIndex: Int) {
        selectedImage = imageIndex
        showDialogState = !showDialogState
    }

    fun onUpdateTransportState(event: TransportChangeEvent) {
        transportUiState = when (event) {
            is TransportChangeEvent.LicensePlateChanged -> {
                transportUiState.copy(licensePlate = event.licensePlate)
            }
            is TransportChangeEvent.DriverChanged -> {
                transportUiState.copy(driverName = event.driverName)
            }
            is TransportChangeEvent.RouteNumberChanged -> {
                transportUiState.copy(routeNumber = event.routeNumber)
            }
            is TransportChangeEvent.CargoNumberChanged -> {
                transportUiState.copy(newCargoNumber = event.cargoNumber)
            }
            is TransportChangeEvent.OriginalCargoNumberChanged -> {
                transportUiState.copy(originalCargoNumber = event.originalCargoNumber)
            }
        }
    }

    fun scanCargoNumber() {
        val scanner = BarcodeScanner(SvkAndroidApplication.appContext, BarcodeFormat.ITF) { cargoNumber ->
            Log.i("ScanScreen", "barcode: $cargoNumber")
            transportUiState = transportUiState.copy(
                newCargoNumber = cargoNumber,
                // Also remove any previous errors for this text field if they exist
                cargoNumberError = "",
            )
        }
        scanner.startScan()
    }

    fun scanRouteNumber() {
        val scanner = BarcodeScanner(SvkAndroidApplication.appContext, BarcodeFormat.QR) { routeNumberBarcode ->
            transportUiState = transportUiState.copy(
                routeNumber = routeNumberBarcode,
                // Also remove any previous errors for this text field if they exist
                routeNumberError = "",
            )
        }
        scanner.startScan()
    }

    fun isRouteNumberValid(): Boolean {
        val routeNumberResult = validators.validateNotEmptyText(transportUiState.routeNumber, "Route Number")
        if (routeNumberResult.successful) { return true }
        transportUiState = transportUiState.copy(routeNumberError = routeNumberResult.errorMessage)
        return false
    }

    fun isCargoNumberValidThenSave(): Boolean {
        val cargoNumberResult = validators.validateNotEmptyText(transportUiState.newCargoNumber, "Cargo Number")

        // Remove the original cargo number from the list (if editing)
        var cargoNumbers = transportUiState.cargoNumbers
        if (transportUiState.isEditingCargoNumber) {
            cargoNumbers = cargoNumbers.filter { it != transportUiState.originalCargoNumber }
        }

        if (cargoNumbers.contains(transportUiState.newCargoNumber)) {
            transportUiState = transportUiState.copy(
                cargoNumberError = "Cargo number was all ready added to this route",
                cargoNumbers = if (transportUiState.isEditingCargoNumber) {
                    cargoNumbers.toMutableList().apply {
                        add(transportUiState.originalCargoNumber)
                    }
                } else {
                    transportUiState.cargoNumbers
                },
            )
            return false
        }

        if (cargoNumberResult.successful) {
            transportUiState = transportUiState.copy(
                cargoNumbers = cargoNumbers.toMutableList().apply {
                    add(transportUiState.newCargoNumber)
                },
                newCargoNumber = "",
                cargoNumberError = "",
            )
            transportUiState = transportUiState.copy(isEditingCargoNumber = false)
            return true
        }
        transportUiState = transportUiState.copy(cargoNumberError = cargoNumberResult.errorMessage)
        return false
    }

    fun clearRouteNumberError() {
        transportUiState = transportUiState.copy(routeNumberError = "")
    }

    fun clearCargoNumberError() {
        transportUiState = transportUiState.copy(cargoNumberError = "")
    }

    fun startEditingCargoNumber() {
        transportUiState = transportUiState.copy(isEditingCargoNumber = true)
    fun selectRoute(
        routeNr : Int
    ) {
        routeNumber = routeNr
        Log.i("TransportViewModel", "select route : $routeNr")

        selectRouteUseCase(routeNumber).onEach { result ->
            when(result) {
                is Resource.Success -> {
                    transportUiState = TransportUiState(
                        routeNumber = result.data!!
                    )
                    println("TransportUIState look like this : $transportUiState")
                }

                is Resource.Error -> {
                    transportUiState = TransportUiState(
                        error = result.message ?: "There was an error finding a rout with number: $routeNr"
                    )
                }

                is Resource.Loading -> {
                   transportUiState = transportUiState.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    init {
        _state.value = TransportUiState(
            isLoading = false,
            error = "",
            images = mutableListOf(
                R.drawable.transport_two,
                R.drawable.transport_three,
                R.drawable.transport_four,
            ),
        )
    }

    fun stopEditingCargoNumber() {
        transportUiState = transportUiState.copy(isEditingCargoNumber = false)
    }
}

sealed class TransportChangeEvent {
    data class RouteNumberChanged(val routeNumber: String) : TransportChangeEvent()
    data class LicensePlateChanged(val licensePlate: String) : TransportChangeEvent()
    data class DriverChanged(val driverName: String) : TransportChangeEvent()
    data class CargoNumberChanged(val cargoNumber: String) : TransportChangeEvent()
    data class OriginalCargoNumberChanged(val originalCargoNumber: String) : TransportChangeEvent()
}
