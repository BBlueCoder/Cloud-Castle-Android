package com.bluecoder.cloudcastle.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SignupScreen(
    authViewModel: AuthViewModel,
    navController: NavController
){
    val scaffoldState = rememberScaffoldState()

    val authState by authViewModel.userAuthenticatingState.collectAsState()

    val focusManager = LocalFocusManager.current

    if(authState != null){
        if(authState!!.isFailure){
            LaunchedEffect(key1 = scaffoldState.snackbarHostState){
                scaffoldState.snackbarHostState.showSnackbar("Signup failed")
                println("Signup failed ${authState!!.exceptionOrNull()?.message}")

            }
        }else{
            LaunchedEffect(key1 = scaffoldState.snackbarHostState){
                scaffoldState.snackbarHostState.showSnackbar("Signup success")
                println("Signup success ${authState!!.getOrNull()?.token}")
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
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
                OutlinedTextField(
                    value = authViewModel.currentUser.username,
                    onValueChange = {authViewModel.updateUser(username = it)},
                    label = { Text(text = "Enter your username") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = authViewModel.currentUser.password,
                    onValueChange = {authViewModel.updateUser(password = it)},
                    label = { Text(text = "Enter your password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.size(20.dp))
                Text(
                    text = "Already have an account? Login",
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
                    Text("Sign up")
                }
            }
        }
    }
}