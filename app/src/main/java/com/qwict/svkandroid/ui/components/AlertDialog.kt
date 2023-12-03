package com.qwict.svkandroid.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    var isTooltipVisible by remember { mutableStateOf(false) }
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "The Icon of the Alert dialog")
        },
        title = {
            Text(
                style = MaterialTheme.typography.headlineMedium,
                text = dialogTitle,
            )
        },
        text = {
            Column {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = dialogText,
                )

                if (isTooltipVisible) {
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        text = dialogText,
                    )
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
//            LongPressButton(
//                toggleTooltipVisible = {
//                    isTooltipVisible = !isTooltipVisible
//                },
//                onLongClick = {
//                    onConfirmation()
//                },
//            ) {
//                Text("Confirm")
//            }
            TextButton(
                onClick = {
                    onConfirmation()
                },

            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                },
            ) {
                Text("Dismiss")
            }
        },
    )
}
