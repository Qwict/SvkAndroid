package com.qwict.svkandroid.ui.viewModels

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.qwict.svkandroid.domain.model.Cargo
import com.qwict.svkandroid.domain.model.Image
import com.qwict.svkandroid.domain.model.Transport
import com.qwict.svkandroid.domain.use_cases.AddImagesUseCase
import com.qwict.svkandroid.domain.use_cases.DeleteActiveTransportUseCase
import com.qwict.svkandroid.domain.use_cases.DeleteImageUseCase
import com.qwict.svkandroid.domain.use_cases.FinishTransportUseCase
import com.qwict.svkandroid.domain.use_cases.GetActiveTransportUseCase
import com.qwict.svkandroid.domain.use_cases.InsertCargoUseCase
import com.qwict.svkandroid.domain.use_cases.SelectRouteUseCase
import com.qwict.svkandroid.domain.use_cases.SetDriverUseCase
import com.qwict.svkandroid.domain.use_cases.UpdateCargoUseCase
import com.qwict.svkandroid.domain.validator.Validators
import com.qwict.svkandroid.ui.screens.BarcodeFormat
import com.qwict.svkandroid.ui.screens.BarcodeScanner
import com.qwict.svkandroid.ui.viewModels.states.DialogUiState
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
    private val deleteActiveTransportUseCase: DeleteActiveTransportUseCase,
    private val setDriverUseCase: SetDriverUseCase,
    private val deleteImageUseCase: DeleteImageUseCase,
    private val getActiveTransportUseCase: GetActiveTransportUseCase,
//    private val repository: SvkRepository,
    private val finishTransportUseCase: FinishTransportUseCase,
) : ViewModel() {
    var transportUiState by mutableStateOf(TransportUiState())
        private set
    var dialogUiState by mutableStateOf(DialogUiState())
        private set


    init {
        getActiveTransportUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("TransportViewModel", "init: ${result.data}")
                    getImagesOnInit(result.data!!.images)
                    transportUiState = transportUiState.copy(
                        routeNumber = result.data!!.routeNumber,
                        licensePlate = result.data.licensePlate,
                        driverName = result.data.driverName,
                        cargoNumbers = result.data.cargos.toMutableList().map { cargo -> cargo.cargoNumber },
                        isLoading = false,
                        images = getImagesOnInit(result.data.images)
                    )

                }

                is Resource.Error -> {
                    transportUiState = transportUiState.copy(
                        error = result.message
                            ?: "There was an error getting the active transport.",
                        isLoading = false,
                    )
                }

                is Resource.Loading -> {
                    transportUiState = transportUiState.copy(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
//
private fun getImagesOnInit(imagesTransport : List<Image>) : Map<UUID, Bitmap> {
        try {
            val images = mutableMapOf<UUID, Bitmap>()
            imagesTransport.forEach { img ->
                val resolver = SvkAndroidApplication.appContext.contentResolver

                val projection = arrayOf(MediaStore.Images.Media.DATA)

                val cursor = resolver.query(img.localUri, projection, null, null, null)

                cursor?.use {
                    val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)

                    while (it.moveToNext()){
                        val filePath = it.getString(columnIndex)
                        val bitmap = BitmapFactory.decodeFile(filePath)
                        images[img.imageUuid] = bitmap
                    }
                }
            }
            return images
        } catch (e: Exception) {
            Log.e("Error", "Error converting image data to images: ${e.message!!}")
            return emptyMap()
        }
    }

    fun onTakePhoto(bitmap: Bitmap) {
        val resolver = SvkAndroidApplication.appContext.contentResolver
        val uuid = UUID.randomUUID()

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DESCRIPTION, uuid.toString())
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val imgUri = resolver.insert(collection, values) ?: return

        imgUri.let { uri ->
            resolver.openOutputStream(uri)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            }

            Log.d("InsertImg", "Image inserted at $uuid")
        }

        transportUiState = transportUiState.copy(
            images = transportUiState.images.toMutableMap().apply { put(uuid, bitmap) },
            imagesError = "",
            imageUris = transportUiState.imageUris.toMutableMap().apply { put(uuid, imgUri) },
        )

        addImagesUseCase(uuid, transportUiState.routeNumber, imgUri).onEach { result ->
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

    fun deleteImageOnIndex(imageUuid: UUID) {
        Log.i("TransportViewModel", "deleteImageOnIndex: $imageUuid")
        val imgUri = transportUiState.imageUris[imageUuid]
        if (!imageUuid.equals(null)) {
//      if (imageIndex >= 0 && imageIndex < transportUiState.images.size) {
            deleteImageUseCase(imageUuid, imgUri!!).onEach { result ->
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
                images = transportUiState.images.toMutableMap().apply { remove(imageUuid) },
//                images = transportUiState.images.toMutableList().apply { remove(imageIndex -> int) },
            )
            Log.i("TransportViewModel", "deleteImageOnIndex: $imageUuid")
        } else {
            // Handle index out of bounds, e.g., throw an exception or log an error
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

    fun isTransportValid(): Boolean {
        val driverNameResult = validators.validateNotEmptyText(transportUiState.driverName, "Driver name")
        val licensePlateResult = validators.validateNotEmptyText(transportUiState.licensePlate, "License plate")

        val cargoNumbersResult = validators.validateNotEmptyList(transportUiState.cargoNumbers, "Cargo numbers")
        val imagesResult = validators.validateNotEmptyList(transportUiState.images.keys.toList(), "Images")

        val hasErrors = listOf(
            driverNameResult,
            licensePlateResult,
            cargoNumbersResult,
            imagesResult,
        ).any {
            !it.successful
        }

        if (!hasErrors) {
            return true
        }
        transportUiState = transportUiState.copy(
            driverNameError = driverNameResult.errorMessage,
            licensePlateError = licensePlateResult.errorMessage,
            cargoNumbersError = cargoNumbersResult.errorMessage,
            imagesError = imagesResult.errorMessage,
        )
        return false
    }

    fun isDriverNameValid(hasDriverName : Boolean, hasLicensePlate: Boolean) {
        val driverNameResult = validators.validateNotEmptyText(transportUiState.driverName, "Driver name")
        val licensePlateResult = validators.validateNotEmptyText(transportUiState.licensePlate, "License plate")

        val hasErrors = when {
            hasDriverName && !hasLicensePlate-> listOf(driverNameResult).any { !it.successful }
            !hasDriverName && hasLicensePlate -> listOf(licensePlateResult).any { !it.successful }
            else -> false
        }

        Log.d("Update", "hasErrors = $hasErrors")
        if (!hasErrors) {
            Log.d("Update", "Updating the licenseplate in local room : ${transportUiState.licensePlate}")
            Log.d("Update", "Updating the drivername in local room : ${transportUiState.driverName}")

            updateLocalTransport()
        }

        transportUiState = transportUiState.copy(
            driverNameError = driverNameResult.errorMessage,
            licensePlateError = licensePlateResult.errorMessage,
        )
    }

    fun isCargoNumberValidThenSave(): Boolean {
        val cargoNumberResult = validators.validateNotEmptyText(transportUiState.newCargoNumber, "Cargo Number")

        var cargoNumbers = transportUiState.cargoNumbers

        if (cargoNumbers.contains(transportUiState.newCargoNumber)) {
            cargoNumbers = cargoNumbers.filter { it != transportUiState.originalCargoNumber }
            transportUiState = transportUiState.copy(
                cargoNumberError = "Cargo number was all ready added to this route",
                cargoNumbers = if (dialogUiState.isCargoDialogOpen) {
                    cargoNumbers.toMutableList().apply {
                        add(transportUiState.originalCargoNumber)
                    }
                }
                else {
                    transportUiState.cargoNumbers
                },
            )
            return false
        }

        if (cargoNumberResult.successful) {
            if (dialogUiState.isCargoDialogOpen) {
                if (transportUiState.cargoNumbers.contains(transportUiState.originalCargoNumber)) {
                    updateCargo(transportUiState.originalCargoNumber, transportUiState.newCargoNumber)
                    // Remove the original cargo number from the list (if editing)
                    cargoNumbers = cargoNumbers.filter { it != transportUiState.originalCargoNumber }
                } else {
                    insertCargo(
                        Cargo(
                            cargoNumber = transportUiState.newCargoNumber,
                            loaderId = getDecodedPayload(getEncryptedPreference("token")).userId,
                            routeNumber = transportUiState.routeNumber,
                        ),
                    )
                }
            }
            transportUiState = transportUiState.copy(
                cargoNumbers = cargoNumbers.toMutableList().apply {
                    add(transportUiState.newCargoNumber)
                },
                newCargoNumber = "",
                cargoNumberError = "",
            )
            dialogUiState = dialogUiState.copy(isCargoDialogOpen = false)

            return true
        }

        transportUiState = transportUiState.copy(cargoNumberError = cargoNumberResult.errorMessage)
        return false
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
                    transportUiState = transportUiState.copy(
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

    private fun updateCargo(oldCargoNumber: String, newCargoNumber: String) {
        Log.i("Update", "Updating cargo in local db")
        updateCargoUseCase(oldCargoNumber, newCargoNumber).onEach { result ->
            when (result) {
                is Resource.Success -> {}
                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun insertCargo(cargo: Cargo) {
        Log.i("Insert", "Insert cargo in local db")
        insertCargoUseCase(cargo = cargo).onEach { result ->
            when (result) {
                is Resource.Success -> {}
                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun finishTransport() {
        transportUiState = transportUiState.copy(isLoading = true)
        finishTransportUseCase(collectTransportFromState()).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.i("Finish", "Successfully finished transport")
                    clearTransportState()
                }
                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun collectTransportFromState(): Transport {
        val loaderId = getDecodedPayload(getEncryptedPreference("token")).userId

        Log.i("collectTransportFromState", "images ${transportUiState.images.keys} image URIs ${transportUiState.imageUris.keys}")

        return Transport(
            routeNumber = transportUiState.routeNumber,
            licensePlate = transportUiState.licensePlate,
            driverName = transportUiState.driverName,
            cargos = transportUiState.cargoNumbers.map { cargoNumber ->
                Cargo(
                    cargoNumber = cargoNumber,
                    routeNumber = transportUiState.routeNumber,
                    loaderId = loaderId,
                )
            },

            images = transportUiState.imageUris.map {
                Image(
                    imageUuid = it.key,
                    localUri = it.value,
                    routeNumber = transportUiState.routeNumber,
                    loaderId = loaderId,
                )
            },
        )
    }

    private fun clearTransportState() {
        transportUiState = TransportUiState()
    }

    private fun updateLocalTransport() {
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
                        licensePlate = result.data.licensePlate,
                    )
                }

                is Resource.Error -> {
                    transportUiState = transportUiState.copy(
                        error = result.message ?: "An unexpected error occurred",
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

    fun onUpdateTransportState(event: TransportChangeEvent) {
        transportUiState = when (event) {
            is TransportChangeEvent.LicensePlateChanged -> {
                //updateLocalRoute()
                transportUiState.copy(licensePlate = event.licensePlate)
            }
            is TransportChangeEvent.DriverChanged -> {
                //updateLocalRoute()
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
            is TransportChangeEvent.SelectedImageChanged -> {
                transportUiState.copy(selectedImage = event.selectedImage)
            }
            is TransportChangeEvent.CargoNumberErrorCleared -> {
                transportUiState.copy(cargoNumberError = "")
            }
        }
    }
    fun onToggleDialogState(event: DialogToggleEvent) {
        dialogUiState = when (event) {
            is DialogToggleEvent.BackAlertDialog -> {
                dialogUiState.copy(isBackAlertDialogOpen = !dialogUiState.isBackAlertDialogOpen)
            }
            is DialogToggleEvent.ImageDialog -> {
                dialogUiState.copy(isImageDialogOpen = !dialogUiState.isImageDialogOpen)
            }
            is DialogToggleEvent.CargoDialog -> {
                dialogUiState.copy(isCargoDialogOpen = !dialogUiState.isCargoDialogOpen)
            }
            is DialogToggleEvent.FinishTransportDialog -> {
                dialogUiState.copy(isFinishTransportDialogOpen = !dialogUiState.isFinishTransportDialogOpen)
            }
        }
    }

    fun deleteActiveTransport() {
        viewModelScope.launch {
            deleteActiveTransportUseCase().onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        clearTransportState()
                        Log.i("Delete", "Successfully deleted transport")
                    }
                    is Resource.Loading -> {
                        Log.i("Delete", "Loading")
                    }
                    is Resource.Error -> {
                        Log.i("Delete", "Failed to delete the active transport")
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}

sealed class TransportChangeEvent {
    data class RouteNumberChanged(val routeNumber: String) : TransportChangeEvent()
    data class LicensePlateChanged(val licensePlate: String) : TransportChangeEvent()
    data class DriverChanged(val driverName: String) : TransportChangeEvent()
    data class CargoNumberChanged(val cargoNumber: String) : TransportChangeEvent()
    data class OriginalCargoNumberChanged(val originalCargoNumber: String) : TransportChangeEvent()
    data class SelectedImageChanged(val selectedImage: Bitmap?) : TransportChangeEvent()
    data object CargoNumberErrorCleared : TransportChangeEvent()
}

sealed class DialogToggleEvent {
    data object BackAlertDialog : DialogToggleEvent()
    data object ImageDialog : DialogToggleEvent()
    data object CargoDialog : DialogToggleEvent()
    data object FinishTransportDialog : DialogToggleEvent()
}
