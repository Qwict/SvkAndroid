package com.qwict.svkandroid.ui.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwict.svkandroid.SvkAndroidApplication
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.domain.use_cases.GetActiveTransportUseCase
import com.qwict.svkandroid.domain.use_cases.SelectRouteUseCase
import com.qwict.svkandroid.domain.validator.Validators
import com.qwict.svkandroid.ui.screens.BarcodeFormat
import com.qwict.svkandroid.ui.screens.BarcodeScanner
import com.qwict.svkandroid.ui.viewModels.states.TransportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TransportViewModel @Inject constructor(
    private val validators: Validators,
    private val selectRouteUseCase: SelectRouteUseCase,
    private val getActiveTransportUseCase: GetActiveTransportUseCase,
) : ViewModel() {
    var transportUiState by mutableStateOf(TransportUiState())
        private set
    var showDialogState by mutableStateOf(false)
        private set
    var selectedImage by mutableStateOf(0)
        private set

    init {
        getActiveTransportUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("TransportViewModel", "init: ${result.data}")
                    transportUiState = transportUiState.copy(
                        routeNumber = result.data!!.routeNumber,
                        licensePlate = result.data.licensePlate,
                        driverName = result.data.driverName,
                        cargoNumbers = result.data.cargos.toMutableList().map { cargo -> cargo.cargoNumber },
//                        images = result.data.images.toMutableList().map { image -> image.imageUuid.toString() },
                    )
                }

                is Resource.Error -> {
                    transportUiState = TransportUiState(
                        error = result.message
                            ?: "There was an error getting the active transport.",
                    )
                }

                is Resource.Loading -> {
                    transportUiState = transportUiState.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

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
        if (routeNumberResult.successful) {
            selectRoute()
            return true
        }
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
    }
    private fun selectRoute() {
        Log.i("TransportViewModel", "select route : ${transportUiState.routeNumber}")

        selectRouteUseCase(transportUiState.routeNumber).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    transportUiState = transportUiState.copy(
                        routeNumber = result.data!!,
                    )
                    println("TransportUIState look like this : $transportUiState")
                }

                is Resource.Error -> {
                    transportUiState = TransportUiState(
                        error = result.message
                            ?: "There was an error finding a rout with number: ${transportUiState.routeNumber}",
                    )
                }

                is Resource.Loading -> {
                    transportUiState = transportUiState.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
    fun stopEditingCargoNumber() {
        transportUiState = transportUiState.copy(isEditingCargoNumber = false)
    }

    fun finishTransport() {
        clearTransportState()
        // TODO: Save the transport to the database (again) and set the is_active_flow to false
    }

    private fun clearTransportState() {
        transportUiState = TransportUiState()
    }
}

sealed class TransportChangeEvent {
    data class RouteNumberChanged(val routeNumber: String) : TransportChangeEvent()
    data class LicensePlateChanged(val licensePlate: String) : TransportChangeEvent()
    data class DriverChanged(val driverName: String) : TransportChangeEvent()
    data class CargoNumberChanged(val cargoNumber: String) : TransportChangeEvent()
    data class OriginalCargoNumberChanged(val originalCargoNumber: String) : TransportChangeEvent()
}
