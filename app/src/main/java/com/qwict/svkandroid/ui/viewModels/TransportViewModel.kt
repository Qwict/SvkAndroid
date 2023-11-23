package com.qwict.svkandroid.ui.viewModels

import android.content.ContentValues
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qwict.svkandroid.SvkAndroidApplication
import com.qwict.svkandroid.common.Resource
import com.qwict.svkandroid.common.getDecodedPayload
import com.qwict.svkandroid.data.local.getEncryptedPreference
import com.qwict.svkandroid.data.repository.SvkRepository
import com.qwict.svkandroid.domain.model.Cargo
import com.qwict.svkandroid.domain.use_cases.AddImagesUseCase
import com.qwict.svkandroid.domain.use_cases.DeleteImageUseCase
import com.qwict.svkandroid.domain.use_cases.GetActiveTransportUseCase
import com.qwict.svkandroid.domain.use_cases.InsertCargoUseCase
import com.qwict.svkandroid.domain.use_cases.SelectRouteUseCase
import com.qwict.svkandroid.domain.use_cases.SetDriverUseCase
import com.qwict.svkandroid.domain.use_cases.UpdateCargoUseCase
import com.qwict.svkandroid.domain.validator.Validators
import com.qwict.svkandroid.ui.screens.BarcodeFormat
import com.qwict.svkandroid.ui.screens.BarcodeScanner
import com.qwict.svkandroid.ui.viewModels.states.TransportUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TransportViewModel @Inject constructor(
    private val validators: Validators,
    private val selectRouteUseCase: SelectRouteUseCase,
    private val insertCargoUseCase: InsertCargoUseCase,
    private val updateCargoUseCase: UpdateCargoUseCase,
    private val addImagesUseCase: AddImagesUseCase,
    private val setDriverUseCase: SetDriverUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
    private val getActiveTransportUseCase: GetActiveTransportUseCase,
    private val repository: SvkRepository,
) : ViewModel() {
    var transportUiState by mutableStateOf(TransportUiState())
        private set
    var showDialogState by mutableStateOf(false)
        private set
    var selectedImage by mutableStateOf<Bitmap?>(null)
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

    fun onTakePhoto(bitmap: Bitmap) {
        val resolver = SvkAndroidApplication.appContext.contentResolver
        var uuid = UUID.randomUUID()

        val values = ContentValues().apply {
            put(MediaStore.Images.Media._ID, uuid.toString())
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATA, transportUiState.routeNumber)
        }

        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val imgUri = resolver.insert(collection, values)

        imgUri?.let {uri ->
            resolver.openOutputStream(uri)?.use {outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            }

            Log.d("InsertImg", "Image inserted at $uuid")
        }

        transportUiState = transportUiState.copy(
            images = transportUiState.images.toMutableMap().apply { put(uuid, bitmap) },
        )

        addImagesUseCase(uuid, transportUiState.routeNumber).onEach { result ->
            when (result) {
                is Resource.Success -> {

                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteImageOnIndex(imageIndex: UUID) {
        Log.i("TransportViewModel", "deleteImageOnIndex: $imageIndex")
        if (!imageIndex.equals(null)) {
//      if (imageIndex >= 0 && imageIndex < transportUiState.images.size) {
            deleteImageUseCase(uuid = imageIndex).onEach { result ->
                when (result) {
                    is Resource.Success -> {

                    }

                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }
                }
            }.launchIn(viewModelScope)

            transportUiState = transportUiState.copy(
                images = transportUiState.images.toMutableMap().apply { remove(imageIndex) },
//                images = transportUiState.images.toMutableList().apply { remove(imageIndex -> int) },
            )
            Log.i("TransportViewModel", "deleteImageOnIndex: $imageIndex")
        } else  {
            // Handle index out of bounds, e.g., throw an exception or log an error
        }
    }

    fun toggleShowDialogState(image: Bitmap?) {
        selectedImage = image
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

    fun isDriverNameLicensePlateValid(): Boolean {
        val driverNameResult = validators.validateNotEmptyText(transportUiState.driverName, "Driver name")
        val licensePlateResult = validators.validateNotEmptyText(transportUiState.licensePlate, "License plate")

        if (driverNameResult.successful && licensePlateResult.successful) {
            updateLocalRoute()
            return true
        }
        transportUiState = transportUiState.copy(
            driverNameError = driverNameResult.errorMessage,
            licensePlateError = licensePlateResult.errorMessage,
        )
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
            if(transportUiState.isEditingCargoNumber){
                updateCargo(transportUiState.originalCargoNumber,transportUiState.newCargoNumber)
            } else {
                insertCargo(
                    Cargo(cargoNumber = transportUiState.newCargoNumber,loaderId = getDecodedPayload(getEncryptedPreference("token")).userId)
                )
            }
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

    private fun updateCargo(oldCargoNumber: String, newCargoNumber: String){
        Log.i("Update", "Updating cargo in local db")
        updateCargoUseCase(oldCargoNumber, newCargoNumber).onEach {result ->
            when (result) {
                is Resource.Success -> {}
                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }.launchIn(viewModelScope)
    }


    private fun insertCargo(cargo: Cargo){
        Log.i("Insert", "Insert cargo in local db")
        insertCargoUseCase(
            cargo = cargo,
            routeNumber =  transportUiState.routeNumber
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {}
                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun stopEditingCargoNumber() {
        transportUiState = transportUiState.copy(isEditingCargoNumber = false)
    }

    fun finishTransport() {
        viewModelScope.launch {
            clearTransportState()
            repository.syncTransports()
        }

        // TODO: Save the transport to the database (again) and set the is_active_flow to false
    }

    private fun clearTransportState() {
        transportUiState = TransportUiState()
    }

    private fun updateLocalRoute() {
        Log.i("Update", "Updating route in local db")
        setDriverUseCase(
            routeNumber = transportUiState.routeNumber,
            licensePlate = transportUiState.licensePlate,
            driver = transportUiState.driverName,
        ).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    transportUiState = transportUiState.copy(
                        driverName = result.data!!.driverName,
                        licensePlate = result.data!!.licensePlate,
                    )
                }

                is Resource.Error -> {
                    transportUiState = TransportUiState(
                        error = result.message ?: "An unexpected error occured",
                    )
                }

                is Resource.Loading -> {
                    transportUiState = transportUiState.copy(
                        isLoading = true,
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}

sealed class TransportChangeEvent {
    data class RouteNumberChanged(val routeNumber: String) : TransportChangeEvent()
    data class LicensePlateChanged(val licensePlate: String) : TransportChangeEvent()
    data class DriverChanged(val driverName: String) : TransportChangeEvent()
    data class CargoNumberChanged(val cargoNumber: String) : TransportChangeEvent()
    data class OriginalCargoNumberChanged(val originalCargoNumber: String) : TransportChangeEvent()
}
