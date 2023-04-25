package com.bluecoder.cloudcastle.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bluecoder.cloudcastle.core.data.FileItem
import com.bluecoder.cloudcastle.ui.screens.Screens
import com.bluecoder.cloudcastle.ui.screens.main.data.FilesData
import com.bluecoder.cloudcastle.utils.Constants
import okhttp3.Headers

@Composable
fun DisplayGrid(
    navController: NavController,
    viewModel : MainViewModel,
    token : String,
    screen : String
) {
    ItemList(navController = navController, token = token, viewModel = viewModel, screen = screen)
}

@Composable
fun Failed(
    errorMessage : String,
    onRetry : () -> Unit
){
    Column {
        Text(
            text = errorMessage,
            textAlign = TextAlign.Center,
            color = Color.LightGray,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}

@Composable()
fun Item(
    navController: NavController,
    token: String,
    fileItem : FileItem
){
    Box(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("${Constants.API_THUMBNAIL_URL}${fileItem.id}")
                .headers(Headers.Builder().apply {
                    add("Authentication",token)
                }.build())
                .crossfade(true)
                .build(),
            contentDescription = "Image",
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun ItemList(
    navController: NavController,
    token: String,
    viewModel: MainViewModel,
    screen: String
){
    val filesList by remember {
        viewModel.filesList
    }
    val resultError by remember {
        viewModel.resultError
    }
    val isLoading by remember {
        viewModel.isLoading
    }

    val data = getItemsFromFilesData(filesList)

    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp)
    ){
        items(data){
            Item(navController = navController, token = token, fileItem = it)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(isLoading){
            CircularProgressIndicator(
                color = Color.Black
            )
        }
        if(resultError.isNotEmpty()){
            Failed(errorMessage = resultError) {
                viewModel.fetchFiles(token,screen)
            }
        }
        if(data.isEmpty() && !isLoading){
            Text(text = "No data found", textAlign = TextAlign.Center)
        }
    }
}

fun getItemsFromFilesData(data : FilesData): List<FileItem> {
    return when(data){
        is FilesData.Images -> {
            data.data
        }
        is FilesData.Videos -> {
            data.data
        }
        is FilesData.Audios -> {
            data.data
        }
        is FilesData.Others -> {
            data.data
        }
    }
}