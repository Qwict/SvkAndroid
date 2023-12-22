package com.qwict.svkandroid.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.qwict.svkandroid.ui.theme.SvkAndroidTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoScreen(onTakePhoto: (Bitmap) -> Unit, capturedImages: List<Bitmap>, goBack: () -> Unit) {
    val applicationContext: Context = LocalContext.current
    var isFlashOn by remember { mutableStateOf(false) }

    val controller = remember {
        LifecycleCameraController(applicationContext).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE,

            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                takePhoto(
                    controller,
                    onTakePhoto,
                    applicationContext,
                )
            }) {
                Icon(imageVector = Icons.Default.Camera, contentDescription = "Camera capture icon")
            }
        },

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            // .padding(paddingValues),
        ) {
            CameraPreview(
                controller = controller,
                modifier = Modifier
                    .fillMaxSize(),
                isFlashOn = isFlashOn,
            )

            if (capturedImages.isNotEmpty()) {
                LastPhotoComponent(
                    modifier = Modifier.align(alignment = Alignment.BottomStart),
                    lastCapturedPhoto = capturedImages.last(),
                    viewPhotos = goBack,
                )
            }

            FlashButton(
                onFlashChange = {
                    isFlashOn = !isFlashOn
                    controller.cameraInfo?.hasFlashUnit().let { hasFlashUnit ->
                        if (hasFlashUnit == true) {
                            controller.imageCaptureFlashMode = if (isFlashOn) {
                                ImageCapture.FLASH_MODE_ON
                            } else {
                                ImageCapture.FLASH_MODE_OFF
                            }
                        }
                    }
                },
                isFlashOn = isFlashOn,
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .padding(16.dp),
            )
        }
    }
}

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier,
    isFlashOn: Boolean,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier,
    )
}

private fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    applicationContext: Context,
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(applicationContext),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true,
                )

                onPhotoTaken(rotatedBitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Couldn't take photo: ", exception)
            }
        },
    )
}

@Composable
private fun LastPhotoComponent(
    modifier: Modifier = Modifier,
    lastCapturedPhoto: Bitmap,
    viewPhotos: () -> Unit,
) {
    Card(
        modifier = modifier
            .size(128.dp)
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp,
        ),
        shape = MaterialTheme.shapes.large,
    ) {
        Image(
            bitmap = lastCapturedPhoto.asImageBitmap(),
            contentDescription = "Last captured photo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.clickable(onClick = viewPhotos),
        )
    }
}

@Composable
fun FlashButton(
    onFlashChange: () -> Unit,
    isFlashOn: Boolean,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onFlashChange,
        modifier = modifier,
    ) {
        Icon(
            imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

