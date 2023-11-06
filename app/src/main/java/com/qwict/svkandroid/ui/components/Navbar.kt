package com.qwict.svkandroid.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.qwict.svkandroid.ui.navigation.Navigations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SvkAndroidAppbar(
    currentScreen: Navigations,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    onLogOutClicked: () -> Unit = {},
) {
    val openAlertDialog = remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(
                stringResource(currentScreen.title),
                style = MaterialTheme.typography.headlineMedium,
            )
        }, // Version here
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = navigateUp,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary),
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Return",
                    )
                }
            }
        },
//        TODO: Should see in mockup what we want here... (this is the top right icon)
        actions = {
            if (currentScreen.route != Navigations.Authenticate.route) {
                IconButton(
                    onClick = { openAlertDialog.value = true },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary),
                ) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "The Account screen",
                    )
                }
            } else {
            }
        },

    )

    when {
        openAlertDialog.value -> {
            AlertDialog(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    onLogOutClicked()
                    openAlertDialog.value = false
                },
                dialogTitle = "Log out?",
                dialogText = "Are you sure you want to log out, Any unsaved data will be lost.",
                icon = Icons.Default.Info,
            )
        }
    }
}
