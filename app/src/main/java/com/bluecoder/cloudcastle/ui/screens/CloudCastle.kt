package com.bluecoder.cloudcastle.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bluecoder.cloudcastle.ui.viewmodels.AuthViewModelInterface
import com.bluecoder.cloudcastle.ui.screens.auth.LoginScreen
import com.bluecoder.cloudcastle.ui.screens.auth.SignupScreen
import com.bluecoder.cloudcastle.ui.screens.main.MainScreen
import com.bluecoder.cloudcastle.ui.viewmodels.MainViewModel
import com.bluecoder.cloudcastle.ui.viewmodels.MainViewModelInterface

@Composable
fun CloudCastle(
    authViewModel: AuthViewModelInterface = viewModel(),
    mainViewModel: MainViewModelInterface = viewModel()
) {
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