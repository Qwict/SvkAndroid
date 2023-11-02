package com.qwict.svkandroid.ui.screens

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.MainViewModel
import com.qwict.svkandroid.ui.theme.SvkAndroidTheme

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
fun RouteEditScreen(nextNav: () -> Unit, photoNav: () -> Unit, viewModel: MainViewModel) {
    var multiFloatingState by remember {
        mutableStateOf(MultiFloatingState.Collapsed)
    }
    val items = listOf(

        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.barcodebitmap),
            label = "Camera",
            identifier = Identifier.CameraFab.name,
        ),
//        MinFabItem(
//            icon = ImageBitmap.imageResource(id = R.drawable.barcodebitmap),
//            label = "Scan barcode",
//            identifier = "CameraFab",
//        ),
        MinFabItem(
            icon = ImageBitmap.imageResource(id = R.drawable.addbitmap),
            label = "Add Load",
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
                nextNav,
            )
        },

    ) { values ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(values),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Route Edit",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall,
            )

            Row {
                LazyRow(userScrollEnabled = true) {
                    items(5) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .width(100.dp)
                                .height(100.dp),
                        )
                    }
                    item {
                        IconButton(
                            modifier = Modifier.padding(8.dp),
                            onClick = { photoNav() },
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        ) {
                            Icon(

                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add photo",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (viewModel.laadBonnen.size == 0) {
                    item {
                        Text(
                            text = "Geen laadbonnen",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                } else {
                    items(viewModel.laadBonnen.size) { laadbon ->
                        ListItem(
                            headlineText = { Text(text = "BarcodeNr") },
                            supportingText = {
                                Text(
                                    text = "1507$laadbon",
                                    style = MaterialTheme.typography.labelSmall,
                                )
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
}

@Composable
fun MultiFloatingButton(
    multiFloatingState: MultiFloatingState,
    onMultiFabStateChange: (MultiFloatingState) -> Unit,
    items: List<MinFabItem>,
    nextNav: () -> Unit,
) {
    val transition = updateTransition(targetState = multiFloatingState, label = "transition")
    val rotate by transition.animateFloat(label = "rotate") {
        if (it == MultiFloatingState.Expanded) 315f else 0f
    }
    val fabScale by transition.animateFloat(label = "Fabscale") {
        if (it == MultiFloatingState.Expanded) 36f else 0f
    }

    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = { tween(durationMillis = 200) },
    ) {
        if (it == MultiFloatingState.Expanded) 1f else 0f
    }
    val textShadow by transition.animateDp(
        label = "textShadow",
        transitionSpec = { tween(durationMillis = 200) },
    ) {
        if (it == MultiFloatingState.Expanded) 2.dp else 0.dp
    }

    Column(horizontalAlignment = Alignment.End) {
        if (transition.currentState == MultiFloatingState.Expanded) {
            items.forEach {
                MinFab(item = it, onMinFabItemClick = { minFabItem ->
                    when (minFabItem.identifier) {
                        Identifier.CameraFab.name -> {
                            // TODO START CAMERA
                        }

                        Identifier.AddLoadFab.name -> {
                            nextNav()
                        }
                    }
                }, alpha = alpha, textShadow = textShadow, fabScale = fabScale)
                Spacer(modifier = Modifier.size(24.dp))
            }
        }
        FloatingActionButton(
            onClick = {
                onMultiFabStateChange(
                    if (transition.currentState == MultiFloatingState.Expanded) {
                        MultiFloatingState.Collapsed
                    } else {
                        MultiFloatingState.Expanded
                    },
                )
            },

        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.rotate(rotate),
            )
        }
    }
}

@Composable
fun MinFab(
    item: MinFabItem,
    alpha: Float,
    textShadow: Dp,
    fabScale: Float,
    showLabel: Boolean = true,
    onMinFabItemClick: (MinFabItem) -> Unit,

) {
    val buttonColor = MaterialTheme.colorScheme.primary
    val shadow = Color.Black.copy(.5f)
    Row {
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
                            animationSpec = tween(50),
                            label = "",
                        ).value,
                    )
                    .shadow(textShadow)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(start = 8.dp, end = 8.dp),

            )
            Spacer(modifier = Modifier.size(16.dp))
        }

        Canvas(
            modifier = Modifier
                .size(42.dp)
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
//            drawCircle(
//                color = shadow, radius = fabScale, center = Offset(
//                    center.x + 2f, center.y + 2f
//                )
//            )

//            drawCircle(
//                color = buttonColor,
//                radius = 80f,
//
//                )
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RouteEditScreenPreview() {
    SvkAndroidTheme(darkTheme = false) {
        RouteEditScreen(nextNav = {}, photoNav = {}, viewModel = MainViewModel())
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RouteEditDarkScreenPreview() {
    SvkAndroidTheme(darkTheme = true) {
        RouteEditScreen(nextNav = {}, photoNav = {}, viewModel = MainViewModel())
    }
}
