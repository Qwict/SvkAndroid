package com.qwict.svkandroid.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable function for rendering an authentication button.
 *
 * @param text The text to be displayed on the button.
 * @param onClick Callback function to be invoked when the button is clicked.
 */
@Composable
fun AuthButton(
    text: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp),
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
            )
        }
    }
}
