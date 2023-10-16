package com.bluecoder.cloudcastle.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.screens.Screens
import com.bluecoder.cloudcastle.ui.screens.main.components.DocumentsSection
import com.bluecoder.cloudcastle.ui.screens.main.components.FABAdd
import com.bluecoder.cloudcastle.ui.screens.main.components.PhotosSection
import com.bluecoder.cloudcastle.ui.screens.main.components.SoundsSection
import com.bluecoder.cloudcastle.ui.theme.Black


@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel : MainViewModel = hiltViewModel()
) {

    val photosUiState by mainViewModel.photosState.collectAsStateWithLifecycle()
    val documentsUiState by mainViewModel.documentsUiState.collectAsStateWithLifecycle()
    val soundsUiState by mainViewModel.soundsUiState.collectAsStateWithLifecycle()

    val imagesCount by mainViewModel.imagesCount.collectAsStateWithLifecycle()
    val videosCount by mainViewModel.videosCount.collectAsStateWithLifecycle()
    val documentsCount by mainViewModel.documentsCount.collectAsStateWithLifecycle()
    val soundsCount by mainViewModel.soundsCount.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
        ,
        floatingActionButton = {
            FABAdd()
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ){
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Home",
                        style = MaterialTheme.typography.titleMedium,
                        color = Black
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_account),
                        contentDescription = null,
                        tint = Black
                    )
                }

                Spacer(modifier = Modifier.height(80.dp))

                PhotosSection(photosUiState = photosUiState,imagesCount,videosCount) {
                    navController.navigate("${Screens.ContentScreen.name}/image,video")
                }
                
                Spacer(modifier = Modifier.height(50.dp))

                DocumentsSection(documentsUiState = documentsUiState,documentsCount) {
                    navController.navigate("${Screens.ContentScreen.name}/application")
                }

                Spacer(modifier = Modifier.height(50.dp))

                SoundsSection(soundsUiState = soundsUiState, soundsCount = soundsCount) {
                    navController.navigate("${Screens.ContentScreen.name}/audio")
                }
            }
        }
    }
}

