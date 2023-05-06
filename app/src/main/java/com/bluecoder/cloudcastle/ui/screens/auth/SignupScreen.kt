package com.bluecoder.cloudcastle.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.viewmodels.AuthViewModelInterface
import com.bluecoder.cloudcastle.utils.Utils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    authViewModel: AuthViewModelInterface,
    navController: NavController
){
    val snackBarHostState = remember{
        SnackbarHostState()
    }

    val usernameErrorState = authViewModel.usernameErrorState

    val passwordErrorState = authViewModel.passwordErrorState

    val authState by authViewModel.userAuthenticatingUIState.collectAsState()

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    var token = ""

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
                token = authState!!.getOrNull()?.token?: ""
                navController.navigate("main/$token"){
                    popUpTo("signup"){
                        inclusive = true
                    }
                }
            }
        }
    }
    Scaffold(
        snackbarHost  = { SnackbarHost(snackBarHostState) },
        modifier = Modifier.padding(16.dp)
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
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
                    text = stringResource(id = R.string.hasAccount),
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        authViewModel.clearUserData()
                        navController.navigate("login")
                    }
                )
                Button(onClick = {
                    focusManager.clearFocus()
                    authViewModel.signup()
                }) {
                    Text(stringResource(id = R.string.signup))
                }
            }
        }
    }
}