package com.qwict.svkandroid.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import com.qwict.svkandroid.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LongPressButton(
    toggleTooltipVisible: () -> Unit,
    onLongClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val haptics = LocalHapticFeedback.current
    Surface(
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    toggleTooltipVisible()
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    onLongClick()
                },
                onClickLabel = stringResource(R.string.press_long_to_delete_click_lbl),
            ),
    ) {
        content()
    }
}
