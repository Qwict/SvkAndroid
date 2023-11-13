import android.annotation.SuppressLint
import android.provider.CalendarContract.Colors
import androidx.camera.core.ZoomState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.qwict.svkandroid.R
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageDialog(
    onDismissRequest: () -> Unit,
    imageUrl: Int,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
            Column(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.8f),
                                Color.Black
                            )
                        )
                    )
                    .fillMaxSize()
                ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))

                ) {
                    ZoomableImage(imageUrl)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onDismissRequest() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Dismiss")
                }
            }

    }
}


@ExperimentalGlideComposeApi
@Composable
fun ZoomableImage(model: Any, contentDescription: String? = null) {
    val angle by remember { mutableStateOf(0f) }
    var zoom by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp.value
    val screenHeight = configuration.screenHeightDp.dp.value

    GlideImage(model,
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .graphicsLayer(
                scaleX = zoom, scaleY = zoom, rotationZ = angle
            )
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures(onGesture = { _, pan, gestureZoom, _ ->
                    zoom = (zoom * gestureZoom).coerceIn(1F..4F)
                    if (zoom > 1) {
                        val x = (pan.x * zoom)
                        val y = (pan.y * zoom)
                        val angleRad = angle * PI / 180.0

                        offsetX =
                            (offsetX + (x * cos(angleRad) - y * sin(angleRad)).toFloat()).coerceIn(
                                -(screenWidth * zoom)..(screenWidth * zoom)
                            )
                        offsetY =
                            (offsetY + (x * sin(angleRad) + y * cos(angleRad)).toFloat()).coerceIn(
                                -(screenHeight * zoom)..(screenHeight * zoom)
                            )
                    } else {
                        offsetX = 0F
                        offsetY = 0F
                    }
                })
            })

}
