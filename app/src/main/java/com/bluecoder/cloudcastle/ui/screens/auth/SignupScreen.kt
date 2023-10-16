package com.bluecoder.cloudcastle.ui.screens.auth

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.screens.auth.components.AuthScreen

@Composable
fun SignupScreen(
    authViewModel: AuthViewModel,
    navController: NavController
){
    val snackBarHostState = remember{
        SnackbarHostState()
    }

    val usernameErrorState = authViewModel.usernameErrorState

    val passwordErrorState = authViewModel.passwordErrorState

    val authState by authViewModel.userAuthenticatingUIState.collectAsState()

    val focusManager = LocalFocusManager.current

    if(authState != null){
        if(authState!!.isFailure){
            val failedSignupMsg = stringResource(id = R.string.failed_login)
            LaunchedEffect(key1 = snackBarHostState){
                snackBarHostState.showSnackbar(authState!!.exceptionOrNull()?.message?: failedSignupMsg)
                println("Signup failed ${authState!!.exceptionOrNull()?.message}")
            }
        }else{
            LaunchedEffect(key1 = snackBarHostState){
                println("SignupTAG **************** Signup success ${authState!!.getOrNull()?.token}")
                navController.navigate("main"){
                    popUpTo("signup"){
                        inclusive = true
                    }
                }
            }
        }
    }
    Scaffold(
        snackbarHost  = { SnackbarHost(snackBarHostState) }
    ) { contentPadding ->

        AuthScreen(
            stringResource(id = R.string.signup),
            contentPadding = contentPadding,
            usernameValue = authViewModel.currentUser.value.username,
            usernameErrorState = usernameErrorState,
            updateUsername = {
                authViewModel.updateUser(username = it, null)
            },
            passwordValue = authViewModel.currentUser.value.password,
            passwordErrorState = passwordErrorState,
            updatePassword = {
                authViewModel.updateUser(password = it, username = null)
            },
            secondaryBtn = {
                authViewModel.clearUserData()
                navController.navigate("login")
            },
            secondaryText = stringResource(id = R.string.hasAccount),
            mainBtn = {
                focusManager.clearFocus()
                authViewModel.signup()
            },
            mainText = stringResource(id = R.string.signup)
        )
    }
}