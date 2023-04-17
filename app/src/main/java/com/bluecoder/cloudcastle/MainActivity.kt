package com.bluecoder.cloudcastle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bluecoder.cloudcastle.ui.screens.auth.AuthViewModel
import com.bluecoder.cloudcastle.ui.screens.auth.LoginScreen
import com.bluecoder.cloudcastle.ui.screens.auth.SignupScreen
import com.bluecoder.cloudcastle.ui.theme.CloudCastleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]


        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "login"){
                composable("login"){
                    LoginScreen(authViewModel = authViewModel,navController)
                }
                composable("signup"){
                    SignupScreen(authViewModel = authViewModel,navController)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CloudCastleTheme {
        Greeting("Android")
    }
}