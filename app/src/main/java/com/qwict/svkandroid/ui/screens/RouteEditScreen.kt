package com.qwict.svkandroid.ui.screens

import ImageDialog
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.components.AddCargoNumberDialog
import com.qwict.svkandroid.ui.components.AlertDialog
import com.qwict.svkandroid.ui.components.ImageListItem
import com.qwict.svkandroid.ui.components.MultiFloatingButton
import com.qwict.svkandroid.ui.components.ShakingTextFieldWithIconAndFocus
import com.qwict.svkandroid.ui.components.animateText
import com.qwict.svkandroid.ui.viewModels.DialogToggleEvent
import com.qwict.svkandroid.ui.viewModels.TransportChangeEvent
import com.qwict.svkandroid.ui.viewModels.states.DialogUiState
import com.qwict.svkandroid.ui.viewModels.states.TransportUiState
import java.util.UUID

enum class MultiFloatingState {
    Expanded, Collapsed
}

class MinFabItem(
    val icon: ImageBitmap,
    val label: String,
    val identifier: String,
)

enum class Identifier {
    CameraFab, AddLoadFab
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteEditScreen(
    // navigation
    navigateToRouteScreen: () -> Unit,
    navigateToPhotoRoute: () -> Unit,

    // states
    transportUiState: TransportUiState,
    onUpdateTransportState: (TransportChangeEvent) -> Unit,
    dialogUiState: DialogUiState,
    onToggleDialogState: (DialogToggleEvent) -> Unit,

    // Validators
    isCargoNumberValidThenSave: () -> Boolean,
    isTransportValid: () -> Boolean,

    // Business logic
    finishTransport: () -> Unit,
    deleteActiveTransport: () -> Unit,
    deleteImageOnIndex: (UUID) -> Unit,
    scanCargoNumber: () -> Unit,
    isDriverNameLicenseplateValid: (Boolean, Boolean) -> Unit,
//    updateLocalTransport : () -> Unit
) {
    var multiFloatingState by remember {
        mutableStateOf(MultiFloatingState.Collapsed)
    }

    // For shaking text fields
    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current
    val offsetXLicensePlate = remember { Animatable(0f) }
    val offsetXDriverName = remember { Animatable(0f) }
    val offsetXImageButton = remember { Animatable(0f) }
    val offsetXCargoNumbers = remember { Animatable(0f) }

    BackHandler {
        onToggleDialogState(DialogToggleEvent.BackAlertDialog)
    }

    val items = listOf(
        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.camerabitmap),
            label = stringResource(R.string.image_btn_floating),
            identifier = Identifier.CameraFab.name,
        ),

        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.addbitmap),
            label = stringResource(R.string.cargo_btn_floating),
            identifier = Identifier.AddLoadFab.name,

        ),

    )

    Scaffold(
        floatingActionButton = {
            MultiFloatingButton(
                multiFloatingState = multiFloatingState,
                onMultiFabStateChange = {
                    multiFloatingState = it
                },
                items = items,
                navigateToPhotoRoute = navigateToPhotoRoute,
                openAddCargoNumberDialog = { onToggleDialogState(DialogToggleEvent.CargoDialog) },
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .weight(1f),
                    onClick = {
                        Log.e("RouteEditScreen", "Length of images: ${ transportUiState.images.keys.size }")

                        if (isTransportValid()) {
                            onToggleDialogState(DialogToggleEvent.FinishTransportDialog)
                        } else {
                            // TODO: the isNotEmpty is more efficient, but is not instant (only after second click)
                            if (transportUiState.licensePlate.isBlank()) {
                                animateText(offsetXLicensePlate, coroutineScope, view)
                            }
                            if (transportUiState.driverName.isBlank()) {
                                animateText(offsetXDriverName, coroutineScope, view)
                            }
                            if (transportUiState.cargoNumbers.isEmpty()) {
                                animateText(offsetXCargoNumbers, coroutineScope, view)
                            }
                            if (transportUiState.images.keys.isEmpty()) {
                                animateText(offsetXImageButton, coroutineScope, view)
                            }
                        }
                    },
                ) {
                    Text(
                        text = stringResource(R.string.finish_transport_btn),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
//            Greeting(name = "RouteEditScreen")
            Text(
                text = stringResource(R.string.route_number_txt, transportUiState.routeNumber),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(modifier = Modifier.size(10.dp))
            ShakingTextFieldWithIconAndFocus(
                key = "DriverNameTextField",
                textFieldValue = transportUiState.driverName,
                onValueChange = {
                    onUpdateTransportState(TransportChangeEvent.DriverChanged(it))
                },
                onFocusChanged = { isFocused ->
                    if (!isFocused) {
                        isDriverNameLicenseplateValid(true, false)
                    }
                },
                label = stringResource(R.string.driver_name_txt_fld),
                errorText = transportUiState.driverNameError,
                offsetX = offsetXDriverName,
                isError = transportUiState.driverNameError.isNotEmpty(),
                leadingIcon = Icons.Default.Person,
            )
            ShakingTextFieldWithIconAndFocus(
                key = "LicensePlateTextField",
                textFieldValue = transportUiState.licensePlate,
                onValueChange = {
                    onUpdateTransportState(TransportChangeEvent.LicensePlateChanged(it))
                },
                onFocusChanged = { isFocused ->
                    if (!isFocused) {
                        isDriverNameLicenseplateValid(false, true)
                    }
                },
                label = stringResource(R.string.license_plate_txt_fld),
                errorText = transportUiState.licensePlateError,
                offsetX = offsetXLicensePlate,
                isError = transportUiState.licensePlateError.isNotEmpty(),
                leadingIcon = Icons.Default.LocalShipping,
            )
            Spacer(modifier = Modifier.size(32.dp))
            LazyRow(
                userScrollEnabled = true,
            ) {
                itemsIndexed(transportUiState.images.entries.toList()) { index, image ->
                    Box(
                        modifier = Modifier
                            .width(300.dp)
                            .height(200.dp),
                    ) {
                        // Load property image here
                        Image(
                            bitmap = image.value.asImageBitmap(),
                            // painter = painterResource(id = image),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(2.dp)
                                .clickable {
                                    onUpdateTransportState(
                                        TransportChangeEvent.SelectedImageChanged(
                                            image.value,
                                        ),
                                    )
                                    onToggleDialogState(DialogToggleEvent.ImageDialog)
                                },

                        )
                        IconButton(
                            onClick = { deleteImageOnIndex(image.key) },
                            modifier = Modifier.align(Alignment.TopEnd),
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.primary,
                            ),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(8.dp),
                            )
                        }
                    }
                }
                item {
                    ImageListItem(
                        navigateToPhotoRoute = navigateToPhotoRoute,
                        offsetX = offsetXImageButton,
                    )
                }
            }

            Text(
                text = transportUiState.imagesError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .offset(offsetXImageButton.value.dp, 0.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.size(32.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            ) {
                if (transportUiState.cargoNumbers.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(offsetXCargoNumbers.value.dp, 0.dp),
                            text = stringResource(R.string.no_cargo_numbers_added_yet_txt),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = transportUiState.cargoNumbersError,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(offsetXCargoNumbers.value.dp, 0.dp),
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    items(transportUiState.cargoNumbers.size) { index ->
                        ListItem(
                            headlineText = { Text(text = stringResource(R.string.cargo_number_lst_itm_hdr)) },
                            supportingText = {
                                Row {
                                    Text(
                                        text = transportUiState.cargoNumbers[index],
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }
                            },
                            trailingContent = {
                                IconButton(
                                    onClick = {
                                        onUpdateTransportState(
                                            TransportChangeEvent.CargoNumberErrorCleared,
                                        )
                                        onUpdateTransportState(
                                            TransportChangeEvent.OriginalCargoNumberChanged(
                                                transportUiState.cargoNumbers[index],
                                            ),
                                        )
                                        onUpdateTransportState(
                                            TransportChangeEvent.CargoNumberChanged(
                                                transportUiState.cargoNumbers[index],
                                            ),
                                        )
                                        onToggleDialogState(DialogToggleEvent.CargoDialog)
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary),
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = "The Account screen",
                                    )
                                }
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                headlineColor = MaterialTheme.colorScheme.onPrimary,
                                supportingColor = MaterialTheme.colorScheme.onPrimary,
                            ),
                        )
                    }
                }
            }
        }
    }

    when {
        dialogUiState.isCargoDialogOpen -> {
            AddCargoNumberDialog(
                onConfirmation = {
                    println("Confirmation registered") // Add logic here to handle confirmation.
                },
                scanCargoNumber = { scanCargoNumber() },
                transportUiState = transportUiState,
                onUpdateTransportState = onUpdateTransportState,
                onToggleDialogState = onToggleDialogState,
                isValidAndAddCargoNumber = { isCargoNumberValidThenSave() },
            )
        }

        dialogUiState.isFinishTransportDialogOpen -> {
            AlertDialog(
                onDismissRequest = { onToggleDialogState(DialogToggleEvent.FinishTransportDialog) },
                dialogTitle = stringResource(R.string.finish_transport_alrt_title),
                dialogText = stringResource(R.string.are_you_sure_you_want_to_finish_this_transport_alrt_txt) +
                    stringResource(R.string.this_final_version_will_be_synced_online_alrt_txt),
                onConfirmation = {
                    finishTransport()
                    onToggleDialogState(DialogToggleEvent.FinishTransportDialog)
                    navigateToRouteScreen()
                },
                icon = Icons.Default.Warning,
            )
        }

        dialogUiState.isBackAlertDialogOpen -> {
            AlertDialog(
                onDismissRequest = {
                    onToggleDialogState(DialogToggleEvent.BackAlertDialog)
                },
                onConfirmation = {
                    navigateToRouteScreen()
                    deleteActiveTransport()
                    onToggleDialogState(DialogToggleEvent.BackAlertDialog)
                },
                dialogTitle = stringResource(R.string.cancel_transport_alrt_title),
                dialogText = stringResource(R.string.this_transport_will_be_deleted_are_you_sure_alrt_txt),
                icon = Icons.Default.Info,
            )
        }

        dialogUiState.isImageDialogOpen -> {
            transportUiState.selectedImage?.let {
                ImageDialog(
                    onDismissRequest = {
                        onToggleDialogState(DialogToggleEvent.ImageDialog)
                    },
                    image = it,
                )
            }
        }
    }
}
