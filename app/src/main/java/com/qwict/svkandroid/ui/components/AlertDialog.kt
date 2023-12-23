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
import androidx.compose.ui.res.stringResource
import com.qwict.svkandroid.R

/**
 * Displays a custom AlertDialog with an icon, title, text, and confirmation/dismiss buttons.
 *
 * @param onDismissRequest Callback invoked when the user dismisses the dialog.
 * @param onConfirmation Callback invoked when the user confirms the action.
 * @param dialogTitle The title of the dialog.
 * @param dialogText The text content of the dialog.
 * @param icon The ImageVector representing the icon for the dialog.
 */
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
                Text(stringResource(R.string.confirm_btn))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                },
            ) {
                Text(stringResource(R.string.dismiss_btn))
            }
        },
    )
}
