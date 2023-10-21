package com.bluecoder.cloudcastle.ui.screens.auth

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import com.bluecoder.cloudcastle.CloudCastleTest
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.buildResponse
import com.bluecoder.cloudcastle.data.api.ServerAPI
import com.bluecoder.cloudcastle.data.pojoclasses.UserJWT
import com.bluecoder.cloudcastle.data.repos.UsersRepo
import com.bluecoder.cloudcastle.getString
import com.bluecoder.cloudcastle.invalidUser
import com.bluecoder.cloudcastle.login
import com.bluecoder.cloudcastle.signUp
import com.bluecoder.cloudcastle.ui.screens.content.ContentScreenViewModel
import com.bluecoder.cloudcastle.ui.screens.main.MainViewModel
import com.bluecoder.cloudcastle.utils.SharedPreferencesManager
import com.bluecoder.cloudcastle.validToken
import com.bluecoder.cloudcastle.validUser
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class CloudCastleKtTest{

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var serverAPI: ServerAPI

    @Inject
    lateinit var authViewModel : AuthViewModel

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var contentScreenViewModel: ContentScreenViewModel

    private lateinit var context : android.content.Context

    @Before
    fun setUp() {
        hiltRule.inject()

        context = InstrumentationRegistry.getInstrumentation().context

        authViewModel.clearUserData()

        composeTestRule.setContent {
            CloudCastleTest(
                authViewModel = authViewModel,
                mainViewModel = mainViewModel,
                contentScreenViewModel = contentScreenViewModel
            )
        }

    }

    private fun getString(id : Int): String {
        return getString(composeTestRule,id)
    }

    @Test
    fun testNavigateToSignupScreenThenBackToLoginScreen(){

        composeTestRule.onNodeWithText(getString(composeTestRule,R.string.createAccount))
            .performClick()

        composeTestRule.onAllNodesWithText(getString(composeTestRule,R.string.signup)).assertCountEquals(2)

        composeTestRule.onNodeWithText(getString(composeTestRule,R.string.hasAccount))
            .performClick()

        composeTestRule.onAllNodesWithText(getString(composeTestRule,R.string.login)).assertCountEquals(2)
    }

    @Test
    fun testLoginWithValidUser(){

        login(
            composeTestRule,
            validUser
        )

        composeTestRule.onNodeWithText("Home")
            .assertExists()

    }

    @Test
    fun testLoginWithInvalidUsername(){

        coEvery { serverAPI.login(invalidUser) } returns buildResponse(
            isResponseSuccessful = false,
            errorResponseMessage = getString(composeTestRule,R.string.user_not_found)
        )

        login(
            composeTestRule,
            invalidUser
        )

        composeTestRule.onNodeWithText(
            getString(composeTestRule,R.string.user_not_found)
        ).assertIsDisplayed()
    }

    @Test
    fun testLoginWithInvalidPassword(){

        coEvery { serverAPI.login(invalidUser) } returns buildResponse(
            isResponseSuccessful = false,
            errorResponseMessage = getString(R.string.invalid_password)
        )

        login(
            composeTestRule,
            invalidUser
        )

        composeTestRule.onNodeWithText(
            getString(composeTestRule,R.string.invalid_password)
        ).assertIsDisplayed()
    }

    @Test
    fun testLoginWithEmptyValues(){
        composeTestRule.onAllNodesWithText(getString(composeTestRule,R.string.login))
            .filter(hasClickAction())
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithText(getString(R.string.empty_username_field))
            .assertExists()

        composeTestRule.onNodeWithText(getString(R.string.empty_password_field))
            .assertExists()

        composeTestRule.onNodeWithTag(getString(R.string.username))
            .performClick()
            .performTextInput(validUser.username)

        composeTestRule.onNodeWithTag(getString(R.string.password))
            .performClick()
            .performTextInput(validUser.password)

        composeTestRule.onNodeWithText(getString(R.string.empty_username_field))
            .assertDoesNotExist()

        composeTestRule.onNodeWithText(getString(R.string.empty_password_field))
            .assertDoesNotExist()
    }

    @Test
    fun testSignUpWithValidUser(){

        signUp(
            composeTestRule,
            validUser
        )

        composeTestRule.onNodeWithText("Home")
            .assertExists()
    }

    @Test
    fun testSignUpWithDuplicateUsername(){
        coEvery { serverAPI.signup(invalidUser) } returns buildResponse(
            isResponseSuccessful = false,
            errorResponseMessage = getString(R.string.duplicate_username)
        )

        signUp(
            composeTestRule,
            invalidUser
        )

        composeTestRule.onNodeWithText(getString(R.string.duplicate_username))
            .assertIsDisplayed()
    }

    @Test
    fun testSignUpWithEmptyFields(){
        composeTestRule.onNodeWithText(getString(R.string.createAccount))
            .performClick()

        composeTestRule.onAllNodesWithText(getString(R.string.signup))
            .filter(hasClickAction())
            .onFirst()
            .performClick()

        composeTestRule.onNodeWithText(getString(R.string.empty_username_field))
            .assertExists()

        composeTestRule.onNodeWithText(getString(R.string.empty_password_field))
            .assertExists()

        composeTestRule.onNodeWithTag(getString(R.string.username))
            .performClick()
            .performTextInput(validUser.username)

        composeTestRule.onNodeWithTag(getString(R.string.password))
            .performClick()
            .performTextInput(validUser.password)

        composeTestRule.onNodeWithText(getString(R.string.empty_username_field))
            .assertDoesNotExist()

        composeTestRule.onNodeWithText(getString(R.string.empty_password_field))
            .assertDoesNotExist()
    }
}