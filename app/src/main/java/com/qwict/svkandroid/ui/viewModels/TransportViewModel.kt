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

/**
 * ViewModel for managing transport-related logic and data.
 *
 * This ViewModel is annotated with [HiltViewModel] for Dagger-Hilt integration.
 *
 * @property validators The validators used for input validation.
 * @property selectRouteUseCase The use case responsible for selecting a transport route.
 * @property insertCargoUseCase The use case responsible for inserting cargo data.
 * @property updateCargoUseCase The use case responsible for updating cargo data.
 * @property addImagesUseCase The use case responsible for adding images to cargo.
 * @property deleteActiveTransportUseCase The use case responsible for deleting an active transport.
 * @property setDriverUseCase The use case responsible for setting the driver of a transport.
 * @property deleteImageUseCase The use case responsible for deleting images associated with cargo.
 * @property getActiveTransportUseCase The use case responsible for retrieving active transport information.
 * @property finishTransportUseCase The use case responsible for finishing a transport.
 */
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
    private val finishTransportUseCase: FinishTransportUseCase,
) : ViewModel() {
    var transportUiState by mutableStateOf(TransportUiState())
        private set
    var dialogUiState by mutableStateOf(DialogUiState())
        private set

    /**
     * Initializes the [TransportViewModel] by retrieving information about the active transport.
     *
     * Uses the [GetActiveTransportUseCase] and updates the [TransportUiState] accordingly.
     */
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
                        images = getImagesOnInit(result.data.images),
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

    /**
     * Initiates the process of deleting the active transport, including deleting associated images.
     *
     * Iterates through the images in the current [TransportUiState] and deletes each image using [deleteImageOnIndex].
     * After deleting images, invokes the [DeleteActiveTransportUseCase] to delete the active transport.
     * Clears the transport state upon successful deletion.
     */
    fun deleteActiveTransport() {
        transportUiState.images.forEach { img -> deleteImageOnIndex(img.key) }
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

    /**
     * Retrieves and decodes images associated with the transport during initialization.
     *
     * @param imagesTransport The list of [Image] objects representing images associated with the transport.
     * @return A map of image UUIDs to corresponding [Bitmap] images.
     */
    private fun getImagesOnInit(imagesTransport: List<Image>): Map<UUID, Bitmap> {
        try {
            val images = mutableMapOf<UUID, Bitmap>()
            imagesTransport.forEach { img ->
                val resolver = SvkAndroidApplication.appContext.contentResolver

                val projection = arrayOf(MediaStore.Images.Media.DATA)

                val cursor = resolver.query(img.localUri, projection, null, null, null)

                cursor?.use {
                    val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)

                    while (it.moveToNext()) {
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

    /**
     * Handles the process after taking a photo with the camera, and updates the transport state with the captured image.
     *
     * @param bitmap The [Bitmap] image captured by the camera.
     */
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
                is Resource.Success -> { }
                is Resource.Error -> { }
                is Resource.Loading -> { }
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Deletes the image associated with the specified UUID from the transport state.
     *
     * @param imageUuid The UUID of the image to be deleted.
     */
    fun deleteImageOnIndex(imageUuid: UUID) {
        Log.i("TransportViewModel", "deleteImageOnIndex: $imageUuid")
        val imgUri = transportUiState.imageUris[imageUuid]
        if (!imageUuid.equals(null)) {
//      if (imageIndex >= 0 && imageIndex < transportUiState.images.size) {
            deleteImageUseCase(imageUuid, imgUri!!).onEach { result ->
                when (result) {
                    is Resource.Success -> { }
                    is Resource.Error -> { }
                    is Resource.Loading -> { }
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

    /**
     * Initiates the process of scanning a cargo number using the device's camera.
     *
     * Uses the [BarcodeScanner] to capture the cargo number from the scanned barcode.
     * Updates the [TransportUiState] with the scanned cargo number, removing any previous errors
     * associated with the cargo number field.
     */
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

    /**
     * Initiates the process of scanning a route number using the device's camera.
     *
     * Uses the [BarcodeScanner] to capture the route number from the scanned QR code.
     * Updates the [TransportUiState] with the scanned route number, removing any previous errors
     * associated with the route number field.
     */
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

    /**
     * Checks if the entered route number is valid and performs the necessary actions.
     *
     * Validates the route number using [Validators.validateNotEmptyText] and triggers the [selectRoute] method
     * if the validation is successful. Updates the [TransportUiState] with any validation errors.
     *
     * @return `true` if the route number is valid; otherwise, returns `false`.
     */
    fun isRouteNumberValid(): Boolean {
        val routeNumberResult = validators.validateNotEmptyText(transportUiState.routeNumber, "Route Number")
        if (routeNumberResult.successful) {
            selectRoute()
            return true
        }
        transportUiState = transportUiState.copy(routeNumberError = routeNumberResult.errorMessage)
        return false
    }

    /**
     * Checks if the entered transport details are valid and performs the necessary actions.
     *
     * Validates the driver name, license plate, cargo numbers, and images using various validation methods.
     * Updates the [TransportUiState] with any validation errors and returns `true` if the transport details are valid,
     * otherwise, returns `false`.
     *
     * @return `true` if the transport details are valid; otherwise, returns `false`.
     */
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

    /**
     * Checks if the entered driver name and license plate are valid and performs the necessary actions.
     *
     * Validates the driver name and license plate using various validation methods.
     * Updates the [TransportUiState] with any validation errors and invokes the [updateLocalTransport] method
     * if there are no validation errors.
     *
     * @param hasDriverName `true` if the driver name is provided; otherwise, `false`.
     * @param hasLicensePlate `true` if the license plate is provided; otherwise, `false`.
     */
    fun isDriverNameValid(hasDriverName: Boolean, hasLicensePlate: Boolean) {
        val driverNameResult = validators.validateNotEmptyText(transportUiState.driverName, "Driver name")
        val licensePlateResult = validators.validateNotEmptyText(transportUiState.licensePlate, "License plate")

        val hasErrors = when {
            hasDriverName && !hasLicensePlate -> listOf(driverNameResult).any { !it.successful }
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

    /**
     * Checks if the entered cargo number is valid and saves the cargo information if valid.
     *
     * Validates the cargo number using [Validators.validateNotEmptyText].
     * Updates the [TransportUiState] with any validation errors and performs cargo operations
     * (insert or update) based on the dialog state.
     *
     * @return `true` if the cargo number is valid and saved successfully; otherwise, returns `false`.
     */
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
                } else {
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

    /**
     * Initiates the process of selecting a route based on the current route number in the transport state.
     *
     * Invokes the [SelectRouteUseCase] and updates the [TransportUiState] with the selected route number.
     */
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

    /**
     * Initiates the process of updating cargo information in the local database.
     *
     * Invokes the [UpdateCargoUseCase] to update the cargo number from [oldCargoNumber] to [newCargoNumber].
     */
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

    /**
     * Initiates the process of inserting cargo information into the local database.
     *
     * Invokes the [InsertCargoUseCase] to insert the provided [cargo] information.
     */
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

    /**
     * Initiates the process of finishing the transport and updates the UI state accordingly.
     *
     * Invokes the [FinishTransportUseCase] to finish the transport using the data collected from the current state.
     */
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

    /**
     * Collects and constructs a [Transport] object from the current state in the ViewModel.
     *
     * Uses the data from [transportUiState] to create a [Transport] object, including route number,
     * license plate, driver name, cargo numbers, and images.
     *
     * @return A [Transport] object representing the collected transport data.
     */
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

    /**
     * Initiates the process of updating local transport information.
     *
     * Invokes the [SetDriverUseCase] to update the route information in the local database
     * based on the current state in the ViewModel.
     */
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

    /**
     * Updates the [TransportUiState] based on the provided [TransportChangeEvent].
     *
     * Modifies the [TransportUiState] based on the type of event received, such as changes in license plate,
     * driver name, route number, cargo number, original cargo number, selected image, or clearing cargo number error.
     *
     * @param event The [TransportChangeEvent] to process and update the state.
     */
    fun onUpdateTransportState(event: TransportChangeEvent) {
        transportUiState = when (event) {
            is TransportChangeEvent.LicensePlateChanged -> {
                // updateLocalRoute()
                transportUiState.copy(licensePlate = event.licensePlate)
            }
            is TransportChangeEvent.DriverChanged -> {
                // updateLocalRoute()
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

    /**
     * Toggles the state of various dialogs based on the provided [DialogToggleEvent].
     *
     * Modifies the [DialogUiState] based on the type of event received, such as toggling the back alert dialog,
     * image dialog, cargo dialog, or finish transport dialog.
     *
     * @param event The [DialogToggleEvent] to process and update the dialog state.
     */
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
}

/**
 * Represents events that trigger changes in the transport UI state.
 */
sealed class TransportChangeEvent {
    data class RouteNumberChanged(val routeNumber: String) : TransportChangeEvent()
    data class LicensePlateChanged(val licensePlate: String) : TransportChangeEvent()
    data class DriverChanged(val driverName: String) : TransportChangeEvent()
    data class CargoNumberChanged(val cargoNumber: String) : TransportChangeEvent()
    data class OriginalCargoNumberChanged(val originalCargoNumber: String) : TransportChangeEvent()
    data class SelectedImageChanged(val selectedImage: Bitmap?) : TransportChangeEvent()
    data object CargoNumberErrorCleared : TransportChangeEvent()
}

/**
 * Represents events that trigger toggling the state of various dialogs.
 */
sealed class DialogToggleEvent {
    data object BackAlertDialog : DialogToggleEvent()
    data object ImageDialog : DialogToggleEvent()
    data object CargoDialog : DialogToggleEvent()
    data object FinishTransportDialog : DialogToggleEvent()
}
