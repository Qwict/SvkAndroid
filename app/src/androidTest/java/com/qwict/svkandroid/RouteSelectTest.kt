package com.qwict.svkandroid

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.qwict.svkandroid.ui.screens.RouteScreen
import com.qwict.svkandroid.ui.viewModels.states.TransportUiState
import org.junit.Rule
import org.junit.Test

class RouteSelectTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRouteSelect() {
        composeTestRule.setContent {
            RouteScreen(
                isRouteNumberValid = { true },
                onUpdateTransportState = { _ -> },
                transportUiState = TransportUiState(),
                navigateToRouteEditRoute = {},
                scanRouteNumber = {},
            )
        }

        composeTestRule.onNodeWithText("Routescherm").assertExists()
        composeTestRule.onNodeWithText("Route").assertExists().performClick().performTextInput("12345")
        composeTestRule.onNodeWithText("Scan QR-code").assertHasClickAction()
        composeTestRule.onNodeWithText("Selecteer").assertHasClickAction()
    }
}
