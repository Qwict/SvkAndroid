package com.qwict.svkandroid.ui.screens

import ImageDialog
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.components.AddCargoNumberDialog
import com.qwict.svkandroid.ui.components.AlertDialog
import com.qwict.svkandroid.ui.components.ImageListItem
import com.qwict.svkandroid.ui.components.MultiFloatingButton
import com.qwict.svkandroid.ui.components.ShakingTextFieldWithIcon
import com.qwict.svkandroid.ui.components.animateText
import com.qwict.svkandroid.ui.viewModels.TransportChangeEvent
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
    isTransportValid: () -> Boolean,
    finishTransport: () -> Unit,
    navigateToRouteScreen: () -> Unit,
    onUpdateTransportState: (TransportChangeEvent) -> Unit,
    transportUiState: TransportUiState,
    showDialogState: Boolean,
    selectedImage: Bitmap?,
    deleteImageOnIndex: (UUID) -> Unit,
    toggleShowDialogState: (Bitmap?) -> Unit,
    navigateToPhotoRoute: () -> Unit,
    scanCargoNumber: () -> Unit,
    isCargoNumberValidThenSave: () -> Boolean,
    isDriverNameLicensePlateValid: () -> Boolean,
    clearCargoNumberError: () -> Unit,
    startEditingCargoNumber: () -> Unit,
    stopEditingCargoNumber: () -> Unit
) {
    var multiFloatingState by remember {
        mutableStateOf(MultiFloatingState.Collapsed)
    }

    val openAddCargoNumberDialog = remember { mutableStateOf(false) }
    val openAlertDialog = remember { mutableStateOf(false) }

    // For shaking text fields
    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current
    val offsetXLicensePlate = remember { Animatable(0f) }
    val offsetXDriverName = remember { Animatable(0f) }

//    val transportUiState = transportViewModel.state.collectAsState()
    if (showDialogState) {
        selectedImage?.let {
            ImageDialog(
                onDismissRequest = { toggleShowDialogState(null) },
                image = it,
            )
        }
    }
    val items = listOf(
        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.camerabitmap),
            label = "Image",
            identifier = Identifier.CameraFab.name,
        ),

        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.addbitmap),
            label = "Cargo",
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
                openAddCargoNumberDialog = { openAddCargoNumberDialog.value = true },
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .weight(1f),
                    onClick = {
                        if (isTransportValid()) {
                            openAlertDialog.value = true
                        } else {
                            if (!isDriverNameLicensePlateValid()) {
//                            if (transportUiState.licensePlateError.isNotEmpty()) {
                                animateText(offsetXLicensePlate, coroutineScope, view)
                            }
                            if (!isDriverNameLicensePlateValid()) {
//                            if (transportUiState.driverNameError.isNotEmpty()) {
                                animateText(offsetXDriverName, coroutineScope, view)
                            }
                        }
                    },
                ) {
                    Text(
                        text = "Finish Transport",
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
                text = "Route Number: ${transportUiState.routeNumber}",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(modifier = Modifier.size(32.dp))
            ShakingTextFieldWithIcon(
                textFieldValue = transportUiState.driverName,
                onValueChange = { onUpdateTransportState(TransportChangeEvent.DriverChanged(it)) },
                label = "Driver Name",
                errorText = transportUiState.driverNameError,
                offsetX = offsetXDriverName,
                isError = transportUiState.driverNameError.isNotEmpty(),
                leadingIcon = Icons.Default.Person,
            )
            ShakingTextFieldWithIcon(
                textFieldValue = transportUiState.licensePlate,
                onValueChange = { onUpdateTransportState(TransportChangeEvent.LicensePlateChanged(it)) },
                label = "License Plate",
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
                                    toggleShowDialogState(
                                        image.value,
                                    )
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
                    ImageListItem(navigateToPhotoRoute = navigateToPhotoRoute)
                }
            }

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
                            modifier = Modifier.fillMaxWidth(),
                            text = "No Cargo Numbers added yet",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    items(transportUiState.cargoNumbers.size) { index ->
                        ListItem(
                            headlineText = { Text(text = "Cargo number") },
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
                                        clearCargoNumberError()
                                        startEditingCargoNumber()
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
                                        openAddCargoNumberDialog.value = true
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
        openAddCargoNumberDialog.value -> {
            AddCargoNumberDialog(
                onDismissRequest = { openAddCargoNumberDialog.value = false },
                onConfirmation = {
                    openAddCargoNumberDialog.value = false
                    println("Confirmation registered") // Add logic here to handle confirmation.
                },
                scanCargoNumber = { scanCargoNumber() },
                transportUiState = transportUiState,
                onUpdateTransportState = onUpdateTransportState,
                isValidAndAddCargoNumber = { isCargoNumberValidThenSave() },
                stopEditingCargoNumber = { stopEditingCargoNumber() },
                clearCargoNumberError = { clearCargoNumberError() },
            )
        }
    }

    when {
        openAlertDialog.value -> {
            AlertDialog(
                onDismissRequest = { openAlertDialog.value = false },
                dialogTitle = "Finish Transport",
                dialogText = "Are you sure you want to finish this transport? This action cannot be undone.",
                onConfirmation = {
                    openAlertDialog.value = false
                    finishTransport()
                    navigateToRouteScreen()
                },
                icon = Icons.Default.Warning,
            )
        }
    }
}

data class ImageLocal(
    val id: Long,
    val dateTaken: String,
    val routeNumber: String,
    val uri: Uri
)