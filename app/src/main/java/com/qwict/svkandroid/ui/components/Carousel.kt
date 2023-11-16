package com.qwict.svkandroid.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.qwict.svkandroid.ui.viewModels.states.TransportUiState

@Composable
fun Carousel(
    transportUiState: TransportUiState,
) {
    LazyRow(
        contentPadding = PaddingValues(5.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        itemsIndexed(transportUiState.images) { index, image ->
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(300.dp),
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                )
            }
        }
        item {
            IconButton(
                modifier = Modifier
                    .padding(8.dp)
                    .width(300.dp)
                    .height(200.dp),
                onClick = { },
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            ) {
                Icon(
                    modifier = Modifier.size(64.dp),
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add photo",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
