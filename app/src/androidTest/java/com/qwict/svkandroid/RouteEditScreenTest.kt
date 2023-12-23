package com.qwict.svkandroid

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.qwict.svkandroid.ui.components.AddCargoNumberDialog
import com.qwict.svkandroid.ui.components.AlertDialog
import com.qwict.svkandroid.ui.screens.RouteEditScreen
import com.qwict.svkandroid.ui.viewModels.DialogToggleEvent
import com.qwict.svkandroid.ui.viewModels.states.DialogUiState
import com.qwict.svkandroid.ui.viewModels.states.TransportUiState
import org.junit.Rule
import org.junit.Test

class RouteEditScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun RouteEditScreenFinishTest() {
        composeTestRule.setContent {
            AlertDialog(
                onDismissRequest = { /*TODO*/ },
                dialogTitle = stringResource(R.string.finish_transport_alrt_title),
                dialogText = stringResource(R.string.are_you_sure_you_want_to_finish_this_transport_alrt_txt) + stringResource(
                    R.string.this_final_version_will_be_synced_online_alrt_txt
                ),
                onConfirmation = {

                },
                icon = Icons.Default.Warning,
            )
        }
        composeTestRule.onNodeWithText("Transport Afronden", substring = true, ignoreCase = true)
            .assertExists()
        composeTestRule.onNodeWithText(
            "Weet je zeker dat je dit transport wilt afronden?", substring = true, ignoreCase = true
        ).assertExists()
        composeTestRule.onNodeWithText(
            "Deze definitieve versie wordt online gesynchroniseerd",
            substring = true,
            ignoreCase = true
        ).assertExists()


    }

    @Test
    fun RouteEditScreenErrorTest() {
        composeTestRule.setContent {
            RouteEditScreen(
                navigateToRouteScreen = { /*TODO*/ },
                navigateToPhotoRoute = { /*TODO*/ },
                transportUiState = TransportUiState(
                    routeNumber = "",
                    licensePlate = "",
                    driverName = "",
                    cargoNumbers = listOf(),
                    images = emptyMap(),


                    routeNumberError = "Geen route nummer",
                    licensePlateError = "Geen kenteken",
                    driverNameError = "Geen bestuurder",
                    cargoNumberError = "Geen cargo nummer gevonden",
                    cargoNumbersError = "Geen cargo nummers gevonden",
                    imagesError = "Geen images gevonden",
                    error = "",
                ),
                onUpdateTransportState = { /*TODO*/ },
                dialogUiState = DialogUiState(
                ),
                onToggleDialogState = { /*TODO*/ },
                isCargoNumberValidThenSave = { false },
                isTransportValid = { false },
                finishTransport = { /*TODO*/ },
                deleteActiveTransport = { /*TODO*/ },
                deleteImageOnIndex = { /*TODO*/ },
                scanCargoNumber = { /*TODO*/ },
                isDriverNameLicenseplateValid = { _, _ -> },
            )
        }
        composeTestRule.onNodeWithText("Nog geen vrachtnummers toegevoegd", ignoreCase = true)
            .assertExists()
    }

    @Test
    fun RouteEditAddCargoTest() {
        composeTestRule.setContent {
            AddCargoNumberDialog(onConfirmation = {

            },
                scanCargoNumber = {

                },
                transportUiState = TransportUiState(),
                onUpdateTransportState = {},
                isValidAndAddCargoNumber = { false },
                onToggleDialogState = { _ -> })
        }
        composeTestRule.onNodeWithText("Vrachtnummer", ignoreCase = true).assertExists()
        composeTestRule.onNodeWithText("Opslaan", substring = true, ignoreCase = true)
            .assertExists()
        composeTestRule.onNodeWithText("Annuleren", substring = true, ignoreCase = true)
            .assertExists()
        composeTestRule.onNodeWithText("Scan barcode", substring = true, ignoreCase = true)
            .assertExists()
    }

    @Test
    fun RouteScreenNoTransportsTest() {
        composeTestRule.setContent {
            RouteEditScreen(
                navigateToRouteScreen = {},
                navigateToPhotoRoute = { /*TODO*/ },
                transportUiState = TransportUiState(

                ),
                onUpdateTransportState = { /*TODO*/ },
                dialogUiState = DialogUiState(
                ),
                onToggleDialogState = { /*TODO*/ },
                isCargoNumberValidThenSave = { false },
                isTransportValid = { false },
                finishTransport = { /*TODO*/ },
                deleteActiveTransport = { /*TODO*/ },
                deleteImageOnIndex = { /*TODO*/ },
                scanCargoNumber = { /*TODO*/ },
                isDriverNameLicenseplateValid = { _, _ -> },

                )
        }
        composeTestRule.onNodeWithText("Route Nummer:", substring = true, ignoreCase = true)
            .assertExists()
        composeTestRule.onNodeWithText(
            "Naam Bestuurder", substring = true, ignoreCase = true
        ).assertExists()
        composeTestRule.onNodeWithText(
            "Kenteken", substring = true, ignoreCase = true
        ).assertExists()
        composeTestRule.onNodeWithText(
            "Nog geen vrachtnummers toegevoegd", substring = true, ignoreCase = true
        ).assertExists()

        composeTestRule.onNodeWithText("Transport Afronden", substring = true, ignoreCase = true)
            .assertExists()
    }


}