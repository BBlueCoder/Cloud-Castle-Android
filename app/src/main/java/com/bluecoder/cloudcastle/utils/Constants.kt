package com.bluecoder.cloudcastle.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import com.bluecoder.cloudcastle.ui.screens.Screens
import com.bluecoder.cloudcastle.ui.screens.main.data.BottomNavBarItem

object Constants {
    const val API_BASE_URL = "http://192.168.100.80:3000/api/"
    const val API_THUMBNAIL_URL = "${API_BASE_URL}files/thumbnail/"

    const val FILES_TYPE_IMAGES = "images"
    const val FILES_TYPE_VIDEOS = "videos"
    const val FILES_TYPE_AUDIOS = "audios"
    const val FILES_TYPE_OTHERS = "others"

    val BOTTOM_NAV_BAR_ITEMS = listOf(
        BottomNavBarItem(
            "Images",
            Screens.ImagesScreen.route,
            Icons.Default.Home
        ),
        BottomNavBarItem(
            "Videos",
            Screens.VideosScreen.route,
            Icons.Default.Home
        ),
        BottomNavBarItem(
            "Audios",
            Screens.AudiosScreen.route,
            Icons.Default.Home
        ),
        BottomNavBarItem(
            "Others",
            Screens.OthersScreen.route,
            Icons.Default.Home
        )
    )
}