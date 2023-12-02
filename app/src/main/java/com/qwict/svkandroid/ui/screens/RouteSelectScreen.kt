package com.qwict.svkandroid.ui.screens

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.qwict.svkandroid.ui.components.ShakingTextFieldWithIcon
import com.qwict.svkandroid.ui.components.animateText
import com.qwict.svkandroid.ui.viewModels.TransportChangeEvent
import com.qwict.svkandroid.ui.viewModels.states.TransportUiState

@Composable
fun RouteScreen(
    isRouteNumberValid: () -> Boolean,
    onUpdateTransportState: (TransportChangeEvent) -> Unit,
    transportUiState: TransportUiState,
    navigateToRouteEditRoute: () -> Unit,
    scanRouteNumber: () -> Unit,
) {
    // For shaking text fields
    val coroutineScope = rememberCoroutineScope()
    val view = LocalView.current
    val offsetXRouteNumber = remember { Animatable(0f) }
    var loaded by rememberSaveable { mutableStateOf(true) }

    if (transportUiState.routeNumber.isNotEmpty() && loaded && !transportUiState.isLoading) {
        Log.i("RouteSelectScreen", "LaunchedEffect: ${transportUiState.routeNumber}")
        navigateToRouteEditRoute()
    } else if (!transportUiState.isLoading) {
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
                ShakingTextFieldWithIcon(
                    textFieldValue = transportUiState.routeNumber,
                    onValueChange = {
                        loaded = false
                        onUpdateTransportState(TransportChangeEvent.RouteNumberChanged(it))
                    },
                    label = "Route",
                    isError = transportUiState.routeNumberError.isNotEmpty(),
                    errorText = transportUiState.routeNumberError,
                    offsetX = offsetXRouteNumber,
                    leadingIcon = Icons.Filled.LocalShipping,
                )

                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = {
                        Log.i("RouteSelectScreen", "onClick: ${isRouteNumberValid()}")
                        if (isRouteNumberValid()) {
                            navigateToRouteEditRoute()
                        } else {
                            animateText(offsetXRouteNumber, coroutineScope, view)
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
}
