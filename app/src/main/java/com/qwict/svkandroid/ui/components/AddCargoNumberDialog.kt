package com.qwict.svkandroid.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.viewModels.DialogToggleEvent
import com.qwict.svkandroid.ui.viewModels.TransportChangeEvent
import com.qwict.svkandroid.ui.viewModels.states.TransportUiState

/**
 * Displays an AlertDialog for adding a cargo number.
 *
 * @param onConfirmation Callback invoked when the user confirms the cargo number.
 * @param scanCargoNumber Callback to initiate barcode scanning for cargo numbers.
 * @param transportUiState The current state of the transport UI.
 * @param onUpdateTransportState Callback to update the transport UI state.
 * @param onToggleDialogState Callback to toggle the visibility of the cargo number dialog.
 * @param isValidAndAddCargoNumber Callback to validate and add the cargo number.
 */
@Composable
fun AddCargoNumberDialog(
    onConfirmation: () -> Unit,
    scanCargoNumber: () -> Unit,
    transportUiState: TransportUiState,
    onUpdateTransportState: (TransportChangeEvent) -> Unit,
    onToggleDialogState: (DialogToggleEvent) -> Unit,
    isValidAndAddCargoNumber: () -> Boolean,
) {
    // For shaking text fields
    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current
    val offsetXCargoNumber = remember { Animatable(0f) }

    AlertDialog(
        icon = {
            Icon(Icons.Filled.LocalShipping, contentDescription = "Add a cargo number")
        },
        title = {
            Text(text = stringResource(R.string.add_cargo_number_alrt_title))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ShakingTextField(
                    textFieldValue = transportUiState.newCargoNumber,
                    onValueChange = { onUpdateTransportState(TransportChangeEvent.CargoNumberChanged(it)) },

                    label = stringResource(R.string.cargo_number_alrt_txt),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),

                    errorText = transportUiState.cargoNumberError,
                    offsetX = offsetXCargoNumber,
                    isError = transportUiState.cargoNumberError.isNotEmpty(),

                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { scanCargoNumber() },
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),

                ) {
                    Text(text = stringResource(R.string.btn_scan_barcode), fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        },
        onDismissRequest = {
            onToggleDialogState(DialogToggleEvent.CargoDialog)
        },
        confirmButton = {
            Button(onClick = {
                if (isValidAndAddCargoNumber()) {
                    onConfirmation()
                } else {
                    animateText(offsetXCargoNumber, coroutineScope, view)
                }
            }) {
                Text(stringResource(R.string.save_cargo_btn))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onUpdateTransportState(TransportChangeEvent.CargoNumberChanged(""))
                    onUpdateTransportState(TransportChangeEvent.OriginalCargoNumberChanged(""))
                    onUpdateTransportState(TransportChangeEvent.CargoNumberErrorCleared)
                    onToggleDialogState(DialogToggleEvent.CargoDialog)
                },
            ) {
                Text(stringResource(R.string.cancel_btn))
            }
        },
    )
}
