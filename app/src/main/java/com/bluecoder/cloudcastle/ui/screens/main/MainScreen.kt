package com.bluecoder.cloudcastle.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bluecoder.cloudcastle.core.data.FileItem
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bluecoder.cloudcastle.ui.screens.Screens
import com.bluecoder.cloudcastle.ui.screens.main.data.BottomNavBarItem
import com.bluecoder.cloudcastle.utils.Constants.API_THUMBNAIL_URL
import com.bluecoder.cloudcastle.utils.Constants.BOTTOM_NAV_BAR_ITEMS
import okhttp3.Headers

@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    navController: NavController,
    token: String
) {
    var currentScreen by remember {
        mutableStateOf(Screens.ImagesScreen.route)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(
                items = BOTTOM_NAV_BAR_ITEMS,
                navController = navController,
                currentScreen
            ) {
                currentScreen = it
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            contentAlignment = Center
        ) {
            Column {
                MainLogo()
                Spacer(modifier = Modifier.height(25.dp))
                DisplayGrid(
                    navController = navController,
                    viewModel = mainViewModel,
                    token = token,
                    screen = currentScreen
                )
            }
        }
        mainViewModel.fetchFiles(token,currentScreen)

    }
}

@Composable
fun MainLogo() {
    Text(
        text = "Cloud Castle",
        modifier = Modifier.fillMaxWidth(),
        color = Color.Black,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center
    )
}


@Composable
fun BottomNavBar(
    items: List<BottomNavBarItem>,
    navController: NavController,
    currentScreen: String,
    onItemClick: (String) -> Unit
) {
    BottomNavigation {
        items.forEach { item ->
            val selected = item.route == currentScreen
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    onItemClick(item.route)
                },
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name
                        )
                        if (selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            )
        }
    }
}
