package com.bluecoder.cloudcastle.ui.screens.main

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.bluecoder.cloudcastle.CloudCastleTest
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.data.api.ServerAPI
import com.bluecoder.cloudcastle.fakeFilesData
import com.bluecoder.cloudcastle.getString
import com.bluecoder.cloudcastle.login
import com.bluecoder.cloudcastle.ui.screens.auth.AuthViewModel
import com.bluecoder.cloudcastle.ui.screens.content.ContentScreenViewModel
import com.bluecoder.cloudcastle.validUser
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MainScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var contentScreenViewModel: ContentScreenViewModel


    @Before
    fun setUp() {
        hiltRule.inject()

        val context = InstrumentationRegistry.getTargetContext()
        val config = Configuration.Builder()
            .setExecutor(SynchronousExecutor())
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)

        composeTestRule.setContent {

            CloudCastleTest(
                authViewModel = authViewModel,
                mainViewModel = mainViewModel,
                contentScreenViewModel = contentScreenViewModel
            )
        }
    }

    @Test
    fun testMainScreen(){
        login(
            composeTestRule,
            validUser
        )

        assertCategory(getString(composeTestRule,R.string.photos_category_title))
        assertCategory(getString(composeTestRule,R.string.documents_category_title))
        assertCategory(getString(composeTestRule,R.string.sounds_category_title))


        assertThatCategorySubtitle(
            getString(
                composeTestRule,
                R.string.photos_videos_category_subtitle,
                fakeFilesData.filter { it.fileType.contains("image") }.size,
                fakeFilesData.filter { it.fileType.contains("video") }.size
            )
        )

        assertThatCategorySubtitle(
            getString(
                composeTestRule,
                R.string.documents_category_subtitle,
                fakeFilesData.filter { it.fileType.contains("application") }.size
            )
        )

        assertThatCategorySubtitle(
            getString(
                composeTestRule,
                R.string.sounds_category_subtitle,
                fakeFilesData.filter { it.fileType.contains("audio") }.size
            )
        )
    }

    private fun assertCategory(title : String){
        composeTestRule.onNodeWithText(title)
            .assertExists()
    }

    private fun assertThatCategorySubtitle(subtitle : String){
        composeTestRule.onNode(
            hasText(subtitle)
        )
            .assertExists()
    }

    @Test
    fun testNavigateFromMainScreenToContentScreen(){
        login(
            composeTestRule,
            validUser
        )

        navigateFromSeeAllButtonToContentScreen(0)

        composeTestRule.onNodeWithText(getString(
            composeTestRule,
            R.string.photos_category_title
        ))
            .assertExists()

        navigateBackFromContentScreen()

        navigateFromSeeAllButtonToContentScreen(1)

        composeTestRule.onNodeWithText(
            getString(
                composeTestRule,
                R.string.documents_category_title
            )
        )
            .assertExists()

        navigateBackFromContentScreen()

        navigateFromSeeAllButtonToContentScreen(2)

        composeTestRule.onNodeWithText(
            getString(
                composeTestRule,
                R.string.sounds_category_title
            )
        )
            .assertExists()

    }

    private fun navigateFromSeeAllButtonToContentScreen(index : Int){
        composeTestRule.onAllNodesWithText(
            getString(
                composeTestRule,
                R.string.see_all_btn
            )
        )
            .filter(hasClickAction())[index]
            .performClick()
    }

    private fun navigateBackFromContentScreen(){
        composeTestRule.onNodeWithTag(getString(
            composeTestRule,
            R.string.back_icon_btn
        ))
            .performClick()
    }
}