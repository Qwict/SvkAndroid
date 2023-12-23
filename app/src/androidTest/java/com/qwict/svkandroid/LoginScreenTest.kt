package com.qwict.svkandroid

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import com.qwict.svkandroid.ui.screens.LoginScreen
import com.qwict.svkandroid.ui.viewModels.states.AuthFormState
import com.qwict.svkandroid.ui.viewModels.states.AuthUiState
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testLoginScreen() {
        composeTestRule.setContent {
            LoginScreen(
                onUpdateLoginState = { _ -> },
                login = {},
                authFormState = AuthFormState(),
                authUiState = AuthUiState(),
                switchPasswordVisibility = {},
                navigateToRegisterScreen = {},
                clearValidationErrors = {},
            )
        }
        
        composeTestRule.onAllNodesWithText("Inloggen")[0].assertExists()
        composeTestRule.onNodeWithText("E-mail").assertExists()
        composeTestRule.onNodeWithText("Wachtwoord").assertExists()
        composeTestRule.onAllNodesWithText("Inloggen")[1].assertHasClickAction()
        composeTestRule.onNodeWithText("Account aanvragen").assertExists()
        }
}
