package com.bluecoder.cloudcastle.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bluecoder.cloudcastle.R
import androidx.compose.material3.*
import com.bluecoder.cloudcastle.ui.screens.auth.components.AuthScreen


@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    val usernameErrorState = authViewModel.usernameErrorState

    val passwordErrorState = authViewModel.passwordErrorState

    val authState by authViewModel.userAuthenticatingUIState.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    if (authState != null) {
        val failedLoginMsg = stringResource(id = R.string.failed_login)
        if (authState!!.isFailure) {
            LaunchedEffect(key1 = snackBarHostState) {
                snackBarHostState.showSnackbar(
                    authState!!.exceptionOrNull()?.message ?: failedLoginMsg
                )
                println("Login failed ${authState!!.exceptionOrNull()?.message}")
            }
        } else {
            LaunchedEffect(key1 = "login") {
                println("LoginTAG **************** Login success ${authState!!.getOrNull()?.token}")
                navController.navigate("main") {
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { contentPadding ->

        AuthScreen(
            stringResource(id = R.string.login),
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
                navController.navigate("signup")
            },
            secondaryText = stringResource(id = R.string.createAccount),
            mainBtn = {
                focusManager.clearFocus()
                authViewModel.login()
            },
            mainText = stringResource(id = R.string.login)
        )
    }


}
