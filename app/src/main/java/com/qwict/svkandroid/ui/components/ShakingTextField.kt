package com.qwict.svkandroid.ui.components

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.qwict.svkandroid.ui.theme.SVKTextField
import com.qwict.svkandroid.ui.viewModels.states.AuthUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShakingTextFieldWithIcon(
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorText: String,
    offsetX: Animatable<Float, AnimationVector1D>,
    leadingIcon: ImageVector = Icons.Default.LocalShipping,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    SVKTextField {
        Column() {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { onValueChange(it) },
                label = { Text(label) },
                isError = isError,
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                modifier = Modifier.offset(offsetX.value.dp, 0.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
            )
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.offset(offsetX.value.dp, 0.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShakingPasswordTextField(
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorText: String,
    offsetX: Animatable<Float, AnimationVector1D>,
    leadingIcon: ImageVector = Icons.Default.Lock,
    authState: AuthUiState,
    switchPasswordVisibility: () -> Unit,
) {
    SVKTextField {
        Column(
            modifier = Modifier.offset(offsetX.value.dp, 0.dp),
        ) {
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { onValueChange(it) },
                label = { Text(label) },
                isError = isError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (authState.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
//                modifier = Modifier.offset(offsetX.value.dp, 0.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                trailingIcon = {
                    val image = if (authState.isPasswordVisible) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    }
                    val description =
                        if (authState.isPasswordVisible) "Hide password" else "Show password"
                    IconButton(onClick = switchPasswordVisibility) {
                        Icon(imageVector = image, description)
                    }
                },
            )
            Text(
                text = errorText,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.width(250.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShakingTextField(
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    errorText: String,
    offsetX: Animatable<Float, AnimationVector1D>,
) {
//    SVKTextField {
    Column() {
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { onValueChange(it) },
            label = { Text(label) },
            isError = isError,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.offset(offsetX.value.dp, 0.dp),
            singleLine = true,
        )
        Text(
            text = errorText,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.offset(offsetX.value.dp, 0.dp),
        )
//        }
    }
}

private val shakeKeyframes: AnimationSpec<Float> = keyframes {
    durationMillis = 800
    val easing = FastOutLinearInEasing

    // generate 8 keyframes
    for (i in 1..8) {
        val x = when (i % 3) {
            0 -> 4f
            1 -> -4f
            else -> 0f
        }
        x at durationMillis / 10 * i with easing
    }
}

fun animateText(
    offset: Animatable<Float, AnimationVector1D>,
    coroutineScope: CoroutineScope,
    view: View? = null,
) {
    coroutineScope.launch {
        offset.animateTo(
            targetValue = 0f,
            animationSpec = shakeKeyframes,
        )
    }
    view?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            view.performHapticFeedback(HapticFeedbackConstants.REJECT)
        } else {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }
}
