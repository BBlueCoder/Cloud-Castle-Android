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
import com.bluecoder.cloudcastle.data.pojoclasses.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

fun <T> buildResponse(isResponseSuccessful : Boolean, response : T? = null, errorResponseMessage : String? = null): Response<T> {
    if(isResponseSuccessful)
        return Response.success(response!!)
    val errorBody = errorResponseMessage!!.toResponseBody("application/json".toMediaTypeOrNull())
    return Response.error(401,errorBody)
}

fun getString(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    id : Int
): String {
    return composeTestRule.activity.getString(id)
}

fun login(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    user : User
){
    composeTestRule.onNodeWithTag(getString(composeTestRule,R.string.username))
        .performClick()
        .performTextInput(user.username)

    composeTestRule.onNodeWithTag(getString(composeTestRule,R.string.password))
        .performClick()
        .performTextInput(user.password)

    composeTestRule.onAllNodesWithText(getString(composeTestRule,R.string.login))
        .filter(hasClickAction())
        .onFirst()
        .performClick()
}

fun signUp(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
    user : User
){
    composeTestRule.onNodeWithText(getString(composeTestRule,R.string.createAccount))
        .performClick()

    composeTestRule.onNodeWithTag(getString(composeTestRule,R.string.username))
        .performClick()
        .performTextInput(user.username)

    composeTestRule.onNodeWithTag(getString(composeTestRule,R.string.password))
        .performClick()
        .performTextInput(user.password)

    composeTestRule.onAllNodesWithText(getString(composeTestRule,R.string.signup)).filter(hasClickAction())
        .onFirst()
        .performClick()
}