package com.qwict.svkandroid.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qwict.svkandroid.ui.screens.Identifier
import com.qwict.svkandroid.ui.screens.MinFabItem
import com.qwict.svkandroid.ui.screens.MultiFloatingState

@Composable
fun MultiFloatingButton(
    multiFloatingState: MultiFloatingState,
    onMultiFabStateChange: (MultiFloatingState) -> Unit,
    items: List<MinFabItem>,
    openAddCargoNumberDialog: () -> Unit,
    navigateToPhotoRoute: () -> Unit,
) {
    val transition = updateTransition(targetState = multiFloatingState, label = "transition")
    val rotate by transition.animateFloat(label = "rotate") {
        if (it == MultiFloatingState.Expanded) 315f else 0f
    }
    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = { tween(durationMillis = 100) },
    ) {
        if (it == MultiFloatingState.Expanded) 1f else 0f
    }

    Column(
        horizontalAlignment = Alignment.End,
    ) {
        if (transition.currentState == MultiFloatingState.Expanded) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onTertiary,
                ),
                modifier = Modifier
                    .alpha(
                        animateFloatAsState(
                            targetValue = alpha,
                            animationSpec = tween(100),
                            label = "",
                        ).value,
                    )
                    .width(170.dp),
                elevation = CardDefaults.cardElevation(8.dp),
            ) {
                items.forEach {
                    MinFab(
                        item = it,
                        onMinFabItemClick = { minFabItem ->
                            when (minFabItem.identifier) {
                                Identifier.CameraFab.name -> {
                                    // TODO: Start Camera
                                    navigateToPhotoRoute()
                                }

                                Identifier.AddLoadFab.name -> {
                                    openAddCargoNumberDialog()
//                                    navigateToScanRoute()
                                }
                            }
                        },
                        alpha = alpha,
                    )
                    Spacer(modifier = Modifier.size(12.dp))
                }
            }
        }

        FloatingActionButton(onClick = {
            onMultiFabStateChange(
                if (transition.currentState == MultiFloatingState.Expanded) {
                    MultiFloatingState.Collapsed
                } else {
                    MultiFloatingState.Expanded
                },
            )
        }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.rotate(rotate),
            )
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun MinFab(
    item: MinFabItem,
    alpha: Float,
    showLabel: Boolean = true,
    onMinFabItemClick: (MinFabItem) -> Unit,

) {
    Row(
        Modifier
            .padding(8.dp)
            .alpha(
                animateFloatAsState(
                    targetValue = alpha,
                    animationSpec = tween(100),
                    label = "",
                ).value,
            ),
    ) {
        if (showLabel) {
            Text(
                text = item.label,
                color = MaterialTheme.colorScheme.primary,

                fontSize = 24.sp,

                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .alpha(
                        animateFloatAsState(
                            targetValue = alpha,
                            animationSpec = tween(100),
                            label = "",
                        ).value,
                    )
                    .fillMaxWidth(0.8f),

            )
            Spacer(modifier = Modifier.size(4.dp))
        }

        Canvas(
            modifier = Modifier
                .size(16.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    onClick = {
                        onMinFabItemClick.invoke(item)
                    },
                    indication = rememberRipple(
                        bounded = false,
                        radius = 20.dp,
                        color = MaterialTheme.colorScheme.onSurface,

                    ),
                ),
        ) {
            drawImage(
                image = item.icon,
                topLeft = Offset(
                    center.x - (item.icon.width / 2),
                    center.y - (item.icon.width / 2),
                ),
                alpha = alpha,
            )
        }
    }
}
