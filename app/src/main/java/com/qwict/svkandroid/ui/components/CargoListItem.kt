package com.qwict.svkandroid.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ImageListItem(
    navigateToPhotoRoute: () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(300.dp)
            .height(200.dp),
    ) {
        IconButton(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            onClick = { navigateToPhotoRoute() },
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
