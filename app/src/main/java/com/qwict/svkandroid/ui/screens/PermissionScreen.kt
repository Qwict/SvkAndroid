package com.qwict.svkandroid.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.qwict.svkandroid.ui.theme.SvkAndroidTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(nextNav: () -> Unit) {
    val state = rememberPermissionState(Manifest.permission.CAMERA)
    var navigated by remember { mutableStateOf(false) }
    when {
        state.status.isGranted && !navigated -> {
            nextNav() // next screen
            navigated = true
        }
        else -> {
            LaunchedEffect(Unit) {
                state.launchPermissionRequest()
            }

            Box(
                Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(8.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Text("Camera permission required", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(4.dp))
                        Text("This is required in order for the app to take pictures")
                    }
                }

                // Button is placed below the text elements
                Column(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val context = LocalContext.current
                    Button(
                        modifier = Modifier
                            .width(200.dp)
                            .height(50.dp),
                        onClick = {
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            startActivity(context, intent, null)
                        },
                    ) {
                        Text("Go to settings", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun PermissionScreenPreview() {
    SvkAndroidTheme {
        PermissionScreen({})
    }
}