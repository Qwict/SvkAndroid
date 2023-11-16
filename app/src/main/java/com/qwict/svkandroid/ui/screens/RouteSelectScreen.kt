package com.qwict.svkandroid.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qwict.svkandroid.ui.viewModels.TransportChangeEvent
import com.qwict.svkandroid.ui.viewModels.states.TransportUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    isRouteNumberValid: () -> Boolean,
    onUpdateTransportState: (TransportChangeEvent) -> Unit,
    transportUiState: TransportUiState,
    navigateToRouteEditRoute: () -> Unit,
    scanRouteNumber: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Route Screen",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineLarge,
            )
            OutlinedTextField(
                value = transportUiState.routeNumber,
                onValueChange = {
                    onUpdateTransportState(TransportChangeEvent.RouteNumberChanged(it))
                },
                label = { Text("Route") },
                singleLine = true,
                isError = transportUiState.routeNumberError.isNotEmpty(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
            )
            if (transportUiState.routeNumberError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = transportUiState.routeNumberError,
                    color = MaterialTheme.colorScheme.error,
                )
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }

            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = {
                    Log.i("RouteSelectScreen", "onClick: ${isRouteNumberValid()}")
                    if (isRouteNumberValid()) {
                        navigateToRouteEditRoute()
                    }
                },
            ) {
                Text(text = "Select")
            }
            Button(
                modifier = Modifier.padding(top = 8.dp),
                onClick = { scanRouteNumber() },
            ) {
                Text("Scan QR-code")
            }
        }
    }
}
