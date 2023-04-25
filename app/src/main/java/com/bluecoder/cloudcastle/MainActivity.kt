package com.bluecoder.cloudcastle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bluecoder.cloudcastle.ui.screens.Screens
import com.bluecoder.cloudcastle.ui.screens.auth.AuthViewModel
import com.bluecoder.cloudcastle.ui.screens.auth.LoginScreen
import com.bluecoder.cloudcastle.ui.screens.auth.SignupScreen
import com.bluecoder.cloudcastle.ui.screens.main.MainScreen
import com.bluecoder.cloudcastle.ui.screens.main.MainViewModel
import com.bluecoder.cloudcastle.ui.theme.CloudCastleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        val mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Screens.LoginScreen.route){
                composable(Screens.LoginScreen.route){
                    LoginScreen(authViewModel = authViewModel,navController)
                }
                composable(Screens.SignupScreen.route){
                    SignupScreen(authViewModel = authViewModel,navController)
                }
                composable(
                    Screens.MainScreen.route,
                    arguments = listOf(navArgument("token"){type = NavType.StringType})
                ){
                    MainScreen( navController = navController, token = it.arguments?.getString("token")?: "", mainViewModel = mainViewModel)
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