package com.bluecoder.cloudcastle.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.screens.viewmodels.AuthViewModelMock
import com.bluecoder.cloudcastle.ui.screens.viewmodels.MainViewModelMock
import com.bluecoder.cloudcastle.ui.viewmodels.AuthViewModelInterface
import com.bluecoder.cloudcastle.ui.viewmodels.MainViewModel
import com.bluecoder.cloudcastle.ui.viewmodels.MainViewModelInterface
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CloudCastleKtTest{

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var authViewModel : AuthViewModelInterface
    private val mainViewModel = MainViewModelMock()

    @Before
    fun setUp(){
        authViewModel = AuthViewModelMock(composeTestRule.activity)
        authViewModel.clearUserData()

        composeTestRule.setContent {
            CloudCastle(authViewModel = authViewModel,mainViewModel)
        }
    }

    private fun getString(id : Int): String {
        return composeTestRule.activity.getString(id)
    }

    @Test
    fun testNavigateToSignupScreenThenBackToLoginScreen(){

        composeTestRule.onNodeWithText(getString(R.string.createAccount))
            .performClick()

        composeTestRule.onNodeWithText(getString(R.string.signup)).assertExists()

        composeTestRule.onNodeWithText(getString(R.string.hasAccount))
            .performClick()

        composeTestRule.onNodeWithText(getString(R.string.login)).assertExists()
    }

    @Test
    fun testLoginWithValidUser(){

        composeTestRule.onNodeWithTag(getString(R.string.username))
            .performClick()
            .performTextInput("user")

        composeTestRule.onNodeWithTag(getString(R.string.password))
            .performClick()
            .performTextInput("123")

        composeTestRule.onNodeWithText(getString(R.string.login))
            .performClick()

        composeTestRule.onNodeWithText(getString(R.string.app_name))
            .assertExists()

    }

    @Test
    fun testLoginWithInvalidUsername(){

        composeTestRule.onNodeWithTag(getString(R.string.username))
            .performClick()
            .performTextInput("invalidUser")

        composeTestRule.onNodeWithTag(getString(R.string.password))
            .performClick()
            .performTextInput("123")

        composeTestRule.onNodeWithText(getString(R.string.login))
            .performClick()

        composeTestRule.onNodeWithText(
            getString(R.string.user_not_found)
        ).assertIsDisplayed()
    }

    @Test
    fun testLoginWithInvalidPassword(){

        composeTestRule.onNodeWithTag(getString(R.string.username))
            .performClick()
            .performTextInput("user")

        composeTestRule.onNodeWithTag(getString(R.string.password))
            .performClick()
            .performTextInput("123456")

        composeTestRule.onNodeWithText(getString(R.string.login))
            .performClick()

        composeTestRule.onNodeWithText(
            getString(R.string.invalid_password)
        ).assertIsDisplayed()
    }

    @Test
    fun testLoginWithEmptyValues(){
        composeTestRule.onNodeWithText(getString(R.string.login))
            .performClick()

        composeTestRule.onNodeWithText(getString(R.string.empty_username_field))
            .assertExists()

        composeTestRule.onNodeWithText(getString(R.string.empty_password_field))
            .assertExists()

        composeTestRule.onNodeWithTag(getString(R.string.username))
            .performClick()
            .performTextInput("user")

        composeTestRule.onNodeWithTag(getString(R.string.password))
            .performClick()
            .performTextInput("123")

        composeTestRule.onNodeWithText(getString(R.string.empty_username_field))
            .assertDoesNotExist()

        composeTestRule.onNodeWithText(getString(R.string.empty_password_field))
            .assertDoesNotExist()
    }

    @Test
    fun testSignUpWithValidUser(){

        composeTestRule.onNodeWithText(getString(R.string.createAccount))
            .performClick()

        composeTestRule.onNodeWithTag(getString(R.string.username))
            .performClick()
            .performTextInput("newUser")

        composeTestRule.onNodeWithTag(getString(R.string.password))
            .performClick()
            .performTextInput("123")

        composeTestRule.onNodeWithText(getString(R.string.signup))
            .performClick()

        composeTestRule.onNodeWithText(getString(R.string.app_name))
            .assertExists()
    }

    @Test
    fun testSignUpWithDuplicateUsername(){
        composeTestRule.onNodeWithText(getString(R.string.createAccount))
            .performClick()

        composeTestRule.onNodeWithTag(getString(R.string.username))
            .performClick()
            .performTextInput("user")

        composeTestRule.onNodeWithTag(getString(R.string.password))
            .performClick()
            .performTextInput("123")

        composeTestRule.onNodeWithText(getString(R.string.signup))
            .performClick()

        composeTestRule.onNodeWithText(getString(R.string.duplicate_username))
            .assertIsDisplayed()
    }

    @Test
    fun testSignUpWithEmptyFields(){
        composeTestRule.onNodeWithText(getString(R.string.createAccount))
            .performClick()

        composeTestRule.onNodeWithText(getString(R.string.signup))
            .performClick()

        composeTestRule.onNodeWithText(getString(R.string.empty_username_field))
            .assertExists()

        composeTestRule.onNodeWithText(getString(R.string.empty_password_field))
            .assertExists()

        composeTestRule.onNodeWithTag(getString(R.string.username))
            .performClick()
            .performTextInput("user")

        composeTestRule.onNodeWithTag(getString(R.string.password))
            .performClick()
            .performTextInput("123")

        composeTestRule.onNodeWithText(getString(R.string.empty_username_field))
            .assertDoesNotExist()

        composeTestRule.onNodeWithText(getString(R.string.empty_password_field))
            .assertDoesNotExist()
    }
}