package com.qwict.svkandroid.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.theme.SVKTextfield
import com.qwict.svkandroid.ui.viewModels.TransportChangeEvent
import com.qwict.svkandroid.ui.viewModels.states.TransportUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCargoNumberDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    scanCargoNumber: () -> Unit,
    transportUiState: TransportUiState,
    onUpdateTransportState: (TransportChangeEvent) -> Unit,
    stopEditingCargoNumber: () -> Unit,
    clearCargoNumberError: () -> Unit,
    isValidAndAddCargoNumber: () -> Boolean,
) {
    AlertDialog(
        icon = {
            Icon(Icons.Filled.LocalShipping, contentDescription = "Add a cargo number")
        },
        title = {
            Text(text = "Add Cargo Number")
        },
        text = {
            Column(
//                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SVKTextfield {
                    OutlinedTextField(
                        value = transportUiState.newCargoNumber,
                        onValueChange = { onUpdateTransportState(TransportChangeEvent.CargoNumberChanged(it)) },
                        label = { Text("Cargo Number") },
                        isError = transportUiState.cargoNumberError.isNotEmpty(),
                        singleLine = true,
                    )
                }
                if (transportUiState.cargoNumberError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = transportUiState.cargoNumberError,
                        color = MaterialTheme.colorScheme.error,
                    )
                } else {
                    Spacer(modifier = Modifier.height(20.dp))
                }
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
//                    Button(
//                        onClick = { nextNav() },
//                        modifier = Modifier
//                            .width(200.dp)
//                            .height(50.dp),
//                    ) {
//                        Text(text = stringResource(R.string.btn_enter_manually), fontSize = 20.sp)
//                    }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(onClick = {
                if (isValidAndAddCargoNumber()) {
                    onConfirmation()
                }
            }) {
                Text("Save Cargo")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onUpdateTransportState(TransportChangeEvent.CargoNumberChanged(""))
                    onUpdateTransportState(TransportChangeEvent.OriginalCargoNumberChanged(""))
                    stopEditingCargoNumber()
                    clearCargoNumberError()
                    onDismissRequest()
                },
            ) {
                Text("Cancel")
            }
        },
    )
}
