package com.bluecoder.cloudcastle

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.bluecoder.cloudcastle.data.pojoclasses.FileItem
import com.bluecoder.cloudcastle.data.pojoclasses.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

var validUser = User("user", "password")
var invalidUser = User("invalid", "password")

const val validToken =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODE0MTgzMjEsImRhdGEiOnsiaWQiOjMsInVzZXJuYW1lIjoidXNlcjIifSwiaWF0IjoxNjgxNDE2MjIxfQ.FrF6qw730u1SMAIWy0PsY4YzxJG-a-UWPBpwQuvO0_U"

private const val fakeContentLength = 1024L
private val fakeDate = System.currentTimeMillis()

var fakeFilesData = listOf(
    FileItem(0, "fakeImg", "fakeImg", "image/png", fakeContentLength, fakeDate, null),
    FileItem(1, "fakeImg", "fakeImg", "image/png", fakeContentLength, fakeDate, null),
    FileItem(2, "fakeVideo", "fakeVideo", "video/mp4", fakeContentLength, fakeDate, 8900.00),
    FileItem(3, "fakeImg", "fakeImg", "image/png", fakeContentLength, fakeDate, null),
    FileItem(4, "fakeVideo", "fakeVideo", "video/mp4", fakeContentLength, fakeDate, 12000.44),

    FileItem(5, "fakeFile", "fakeFile", "application/pdf", fakeContentLength, fakeDate, null),
    FileItem(6, "fakeFile", "fakeFile", "application/doc", fakeContentLength, fakeDate, null),
    FileItem(7, "fakeFile", "fakeFile", "application/zip", fakeContentLength, fakeDate, null),
    FileItem(8, "fakeImg", "fakeImg", "audio/mp3", fakeContentLength, fakeDate, null),
    FileItem(9, "fakeVideo", "fakeVideo", "audio/mp3", fakeContentLength, fakeDate, null),
)

var photosFileType = "image,video"
var documentsFileType = "application"
var audiosFileType = "audio"

fun <T> buildResponse(
    isResponseSuccessful: Boolean,
    response: T? = null,
    errorResponseMessage: String? = null
): Response<T> {
    if (isResponseSuccessful)
        return Response.success(response!!)
    val errorBody = errorResponseMessage!!.toResponseBody("application/json".toMediaTypeOrNull())
    return Response.error(401, errorBody)
}

fun getString(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    id: Int,
    vararg args: Any
): String {
    return composeTestRule.activity.getString(id, *args)
}

fun login(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    user: User
) {
    composeTestRule.onNodeWithTag(getString(composeTestRule, R.string.username))
        .performClick()
        .performTextInput(user.username)

    composeTestRule.onNodeWithTag(getString(composeTestRule, R.string.password))
        .performClick()
        .performTextInput(user.password)

    composeTestRule.onAllNodesWithText(getString(composeTestRule, R.string.login))
        .filter(hasClickAction())
        .onFirst()
        .performClick()
}

fun signUp(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    user: User
) {
    composeTestRule.onNodeWithText(getString(composeTestRule, R.string.createAccount))
        .performClick()

    composeTestRule.onNodeWithTag(getString(composeTestRule, R.string.username))
        .performClick()
        .performTextInput(user.username)

    composeTestRule.onNodeWithTag(getString(composeTestRule, R.string.password))
        .performClick()
        .performTextInput(user.password)

    composeTestRule.onAllNodesWithText(getString(composeTestRule, R.string.signup))
        .filter(hasClickAction())
        .onFirst()
        .performClick()
}