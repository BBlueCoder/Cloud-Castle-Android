package com.bluecoder.cloudcastle

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bluecoder.cloudcastle.ui.screens.ScreenArgs
import com.bluecoder.cloudcastle.ui.screens.Screens
import com.bluecoder.cloudcastle.ui.screens.auth.AuthViewModel
import com.bluecoder.cloudcastle.ui.screens.auth.LoginScreen
import com.bluecoder.cloudcastle.ui.screens.auth.SignupScreen
import com.bluecoder.cloudcastle.ui.screens.content.ContentScreen
import com.bluecoder.cloudcastle.ui.screens.content.ContentScreenViewModel
import com.bluecoder.cloudcastle.ui.screens.main.MainScreen
import com.bluecoder.cloudcastle.ui.screens.main.MainViewModel

@Composable
fun CloudCastleTest(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    contentScreenViewModel: ContentScreenViewModel
) {
    NavHost(navController = navController, startDestination = Screens.LoginScreen.route) {
        composable(Screens.LoginScreen.route) {
            LoginScreen(authViewModel = authViewModel, navController)
        }
        composable(Screens.SignupScreen.route) {
            SignupScreen(authViewModel = authViewModel, navController)
        }
        composable(
            Screens.MainScreen.route,
            arguments = getArgumentsList(Screens.MainScreen)
        ) {
            MainScreen(
                navController = navController,
                mainViewModel = mainViewModel
            )
        }
        composable(
            Screens.ContentScreen.route,
            arguments = getArgumentsList(Screens.ContentScreen),
            enterTransition = {
                if(initialState.destination.route!!.contains("main")){
                    slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                }else
                    null
            },
            exitTransition = {
                if (targetState.destination.route!!.contains("main")) {
                    slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                } else
                    null
            }
        ) {
            ContentScreen(
                navController = navController,
                fileType = it.arguments?.getString(ScreenArgs.FileType.name) ?: "",
                viewModel = contentScreenViewModel
            )
        }
    }
}

private fun getArgumentsList(screen: Screens): List<NamedNavArgument> {
    val args = mutableListOf<NamedNavArgument>()
    screen.args.forEach {
        args.add(
            navArgument(
                it.name
            ) {
                type = it.type
            }
        )
    }
    return args
}