package com.bluecoder.cloudcastle.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.viewmodels.AuthViewModelInterface
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import com.bluecoder.cloudcastle.utils.Utils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModelInterface,
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
                val token = authState!!.getOrNull()?.token ?: ""
                navController.navigate("main/$token") {
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = Modifier.padding(16.dp)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                OutlineTextField(
                    value = authViewModel.currentUser.value.username,
                    label = stringResource(id = R.string.enter_username),
                    testTag = stringResource(id = R.string.username),
                    isError = usernameErrorState.value.asString().isNotEmpty(),
                    errorMessage = usernameErrorState.value.asString()
                ) {
                    authViewModel.updateUser(username = it, null)
                }

                OutlineTextField(
                    value = authViewModel.currentUser.value.password,
                    label = stringResource(id = R.string.enter_password),
                    testTag = stringResource(id = R.string.password),
                    transformation = PasswordVisualTransformation(),
                    isError = passwordErrorState.value.asString().isNotEmpty(),
                    errorMessage = passwordErrorState.value.asString()
                ) {
                    authViewModel.updateUser(password = it, username = null)
                }

                Spacer(modifier = Modifier.size(20.dp))
                Text(
                    text = stringResource(id = R.string.createAccount),
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        authViewModel.clearUserData()
                        navController.navigate("signup")
                    }
                )

                Button(onClick = {
                    focusManager.clearFocus()
                    authViewModel.login()
                }) {
                    Text(stringResource(id = R.string.login))
                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlineTextField(
    value: String,
    label: String,
    testTag: String,
    isError: Boolean,
    errorMessage : String,
    transformation: PasswordVisualTransformation? = null,
    onValueChange: (value: String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(text = label) },
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag),
        visualTransformation = transformation ?: VisualTransformation.None,
        singleLine = true,
        isError = isError,
        supportingText = {
            if(isError){
                Text(
                    text = errorMessage,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}

