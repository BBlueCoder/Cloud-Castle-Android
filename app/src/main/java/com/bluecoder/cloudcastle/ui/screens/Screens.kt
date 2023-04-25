package com.bluecoder.cloudcastle.ui.screens

sealed class Screens(val route : String) {
    object LoginScreen : Screens("login")
    object SignupScreen : Screens("signup")
    object MainScreen : Screens("main/{token}")
    object ImagesScreen : Screens("images")
    object VideosScreen : Screens("videos")
    object AudiosScreen : Screens("audios")
    object OthersScreen : Screens("others")
}