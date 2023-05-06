package com.bluecoder.cloudcastle.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.screens.Screens
import com.bluecoder.cloudcastle.ui.screens.main.data.BottomNavBarItem
import com.bluecoder.cloudcastle.ui.viewmodels.MainViewModel
import com.bluecoder.cloudcastle.ui.viewmodels.MainViewModelInterface
import com.bluecoder.cloudcastle.utils.Constants.BOTTOM_NAV_BAR_ITEMS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    mainViewModel: MainViewModelInterface,
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
        text = stringResource(id = R.string.app_name),
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
    NavigationBar {
        items.forEach { item ->
            val selected = item.route == currentScreen
            NavigationBarItem(
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
