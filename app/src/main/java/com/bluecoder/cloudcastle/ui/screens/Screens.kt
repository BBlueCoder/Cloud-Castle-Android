package com.bluecoder.cloudcastle.ui.screens

import androidx.navigation.NavType

sealed class Screens(
    val route: String,
    val name: String = route,
    vararg val args: ScreenArgs,
) {
    object LoginScreen : Screens("login")
    object SignupScreen : Screens("signup")
    object MainScreen : Screens("main")

    object ContentScreen : Screens("content/{file_type}","content",ScreenArgs.FileType)

    object DisplayScreen : Screens(
        "display/{file_id}",
        "display",
        ScreenArgs.FileId
    )

    object ImagesScreen : Screens("images")
    object VideosScreen : Screens("videos")
    object AudiosScreen : Screens("audios")
    object OthersScreen : Screens("others")
}

sealed class ScreenArgs(val name : String,val type : NavType<*>) {
    object Token : ScreenArgs("token", NavType.StringType)

    object FileType : ScreenArgs("file_type", NavType.StringType)

    object FileId : ScreenArgs("file_id", NavType.IntType)

    object X : ScreenArgs("x", NavType.FloatType)
    object Y : ScreenArgs("y", NavType.FloatType)
}

