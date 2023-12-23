package com.qwict.svkandroid.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/**
 * Composable function for rendering a custom outlined button with Svk styling.
 *
 * @param onClick Callback for handling button click.
 * @param text The text to display on the button.
 */
@Composable
fun SvkOutlinedButton(
    onClick: () -> Unit,
    text: String,
) {
    OutlinedButton(
        onClick = { onClick() },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
    ) {
        Text(text = text)
    }
}
