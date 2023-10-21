package com.bluecoder.cloudcastle.ui.screens.content

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.rememberNavController
import androidx.test.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.audiosFileType
import com.bluecoder.cloudcastle.buildResponse
import com.bluecoder.cloudcastle.data.api.ServerAPI
import com.bluecoder.cloudcastle.data.repos.FilesRepo
import com.bluecoder.cloudcastle.documentsFileType
import com.bluecoder.cloudcastle.fakeFilesData
import com.bluecoder.cloudcastle.getString
import com.bluecoder.cloudcastle.photosFileType
import com.bluecoder.cloudcastle.ui.screens.ScreenArgs
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ContentScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var serverAPI: ServerAPI

    @Inject
    lateinit var filesRepo : FilesRepo

    @Before
    fun setUp() {
        hiltRule.inject()

        val context = InstrumentationRegistry.getTargetContext()
        val config = Configuration.Builder()
            .setExecutor(SynchronousExecutor())
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Test
    fun testContentScreenWithPhotos(){
        val savedStateHandle = SavedStateHandle()
        savedStateHandle[ScreenArgs.FileType.name] = photosFileType
        val contentScreenViewModel = ContentScreenViewModel(filesRepo,savedStateHandle)

        composeTestRule.setContent {
            ContentScreen(
                navController = rememberNavController(),
                fileType = photosFileType,
                contentScreenViewModel
            )
        }

        composeTestRule.onNodeWithText(
            getString(
                composeTestRule,
                R.string.photos_category_title
            )
        ).assertExists()

        composeTestRule.onAllNodesWithTag(
            getString(
                composeTestRule,
                R.string.content_item
            )
        ).assertCountEquals(
            fakeFilesData.filter {
                it.fileType.contains("image") || it.fileType.contains("video")
            }.size
        )
    }

    @Test
    fun testEmptyContentScreen(){
        coEvery { serverAPI.getFiles(photosFileType) } returns buildResponse(
            isResponseSuccessful = true,
            response = emptyList()
        )

        val savedStateHandle = SavedStateHandle()
        savedStateHandle[ScreenArgs.FileType.name] = photosFileType
        val contentScreenViewModel = ContentScreenViewModel(filesRepo,savedStateHandle)

        composeTestRule.setContent {
            ContentScreen(
                navController = rememberNavController(),
                fileType = photosFileType,
                contentScreenViewModel
            )
        }

        composeTestRule.onNodeWithText(
            getString(
                composeTestRule,
                R.string.empty_content_category
            )
        ).assertExists()
    }

    @Test
    fun textFailedContentScreen(){
        coEvery { serverAPI.getFiles(photosFileType) } returns buildResponse(
            isResponseSuccessful = false,
            errorResponseMessage = ""
        )

        val savedStateHandle = SavedStateHandle()
        savedStateHandle[ScreenArgs.FileType.name] = photosFileType
        val contentScreenViewModel = ContentScreenViewModel(filesRepo,savedStateHandle)

        composeTestRule.setContent {
            ContentScreen(
                navController = rememberNavController(),
                fileType = photosFileType,
                contentScreenViewModel
            )
        }

        composeTestRule.onNodeWithText(
            getString(
                composeTestRule,
                R.string.content_error_category
            )
        ).assertExists()
    }

    @Test
    fun testContentScreenWithDocuments(){
        val savedStateHandle = SavedStateHandle()
        savedStateHandle[ScreenArgs.FileType.name] = documentsFileType
        val contentScreenViewModel = ContentScreenViewModel(filesRepo,savedStateHandle)

        composeTestRule.setContent {
            ContentScreen(
                navController = rememberNavController(),
                fileType = documentsFileType,
                contentScreenViewModel
            )
        }

        composeTestRule.onNodeWithText(
            getString(
                composeTestRule,
                R.string.documents_category_title
            )
        ).assertExists()

        composeTestRule.onAllNodesWithTag(
            getString(
                composeTestRule,
                R.string.content_item
            )
        ).assertCountEquals(
            fakeFilesData.filter {
                it.fileType.contains("application")
            }.size
        )
    }

    @Test
    fun testContentScreenWithSounds(){
        val savedStateHandle = SavedStateHandle()
        savedStateHandle[ScreenArgs.FileType.name] = audiosFileType
        val contentScreenViewModel = ContentScreenViewModel(filesRepo,savedStateHandle)

        composeTestRule.setContent {
            ContentScreen(
                navController = rememberNavController(),
                fileType = audiosFileType,
                contentScreenViewModel
            )
        }

        composeTestRule.onNodeWithText(
            getString(
                composeTestRule,
                R.string.sounds_category_title
            )
        ).assertExists()

        composeTestRule.onAllNodesWithTag(
            getString(
                composeTestRule,
                R.string.content_item
            )
        ).assertCountEquals(
            fakeFilesData.filter {
                it.fileType.contains("audio")
            }.size
        )
    }

    @Test
    fun testContentScreenSelection(){
        val savedStateHandle = SavedStateHandle()
        savedStateHandle[ScreenArgs.FileType.name] = photosFileType
        val contentScreenViewModel = ContentScreenViewModel(filesRepo,savedStateHandle)

        composeTestRule.setContent {
            ContentScreen(
                navController = rememberNavController(),
                fileType = photosFileType,
                contentScreenViewModel
            )
        }

        composeTestRule.onNodeWithContentDescription(
            getString(
                composeTestRule,
                R.string.menu
            )
        ).performClick()

        composeTestRule.onNodeWithText(
            getString(
                composeTestRule,
                R.string.select_menu_item
            )
        ).performClick()

        composeTestRule.onNodeWithText(
            getString(
                composeTestRule,
                R.string.selected_items_count,
                0
            )
        ).assertExists()

        composeTestRule.onAllNodesWithTag(
            getString(
                composeTestRule,
                R.string.content_item
            )
        ).let {
            it[0].performClick()
            it[2].performClick()
        }

        composeTestRule.onNodeWithText(
            getString(
                composeTestRule,
                R.string.selected_items_count,
                contentScreenViewModel.selectedItemsCount.value
            )
        )

        composeTestRule.onNodeWithTag(
            getString(
                composeTestRule,
                R.string.back_icon_btn
            )
        ).performClick()

        composeTestRule.onNodeWithText(
            getString(
                composeTestRule,
                R.string.selected_items_count,
                0
            )
        ).assertDoesNotExist()

        assertEquals(contentScreenViewModel.selectedItemsCount.value,0)
    }
}