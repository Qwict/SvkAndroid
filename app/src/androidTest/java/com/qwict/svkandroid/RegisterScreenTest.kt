package com.qwict.svkandroid

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.qwict.svkandroid.ui.screens.RegisterScreen
import com.qwict.svkandroid.ui.viewModels.states.AuthFormState
import com.qwict.svkandroid.ui.viewModels.states.AuthUiState
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RegisterScreenTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun testRegisterScreen() {
        var registerUserCalled = false
        rule.setContent {
            RegisterScreen(
                authState = AuthUiState(),
                authFormState = AuthFormState(),
                registerUser = {
                    registerUserCalled = true
                },
                onEvent = { _ -> },
                switchPasswordVisibility = { },
                clearValidationErrors = { },
                navigateToLoginScreen = { }
            )
        }

        rule.onNodeWithText("E-mail").assertExists()
        rule.onNodeWithText("E-mail").performTextInput("test@example.com")
        rule.onNodeWithText("Wachtwoord").performTextInput("password123")
        rule.onNodeWithText("Bevestig Wachtwoord").performTextInput("password123")

        rule.onNodeWithText("Indienen").performClick()
        Assert.assertTrue(registerUserCalled)
    }
}