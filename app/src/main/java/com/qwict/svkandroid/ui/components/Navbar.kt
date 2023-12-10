package com.qwict.svkandroid.ui.components

import androidx.compose.material.icons.Icons
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
import com.qwict.svkandroid.R
import com.qwict.svkandroid.ui.navigation.Navigations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SvkAndroidAppbar(
    currentScreen: Navigations,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    isRouteSelectScreen: Boolean = false,
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
        // TODO: To be honest this button is sh*t,
        //  the user can just press the back button on the phone (or back action) and it would be the same
        //  otherwise... we need to find a way to overwrite this back button action
//        navigationIcon = {
//            if (canNavigateBack) {
//                IconButton(
//                    onClick = navigateUp,
//                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary),
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.ArrowBack,
//                        contentDescription = "Return",
//                    )
//                }
//            }
//        },
//        TODO: Should see in mockup what we want here... (this is the top right icon)
        actions = {
            if (currentScreen.route != Navigations.Authenticate.route && isRouteSelectScreen) {
                IconButton(
                    onClick = { openAlertDialog.value = true },
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary),
                ) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = "The Account screen",
                    )
                }
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
                dialogTitle = stringResource(R.string.log_out_question_alrt_title),
                dialogText = stringResource(R.string.are_you_sure_you_want_to_log_out_alrt_txt) +
                    stringResource(R.string.any_transport_that_was_not_finished_will_not_be_synced_online_alrt_txt),
                icon = Icons.Default.Info,
            )
        }
    }
}
