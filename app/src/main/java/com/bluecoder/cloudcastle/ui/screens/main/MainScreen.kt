package com.bluecoder.cloudcastle.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bluecoder.cloudcastle.core.data.FileItem
import androidx.compose.runtime.*
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bluecoder.cloudcastle.utils.Constants.API_THUMBNAIL_URL
import okhttp3.Headers
import java.lang.reflect.Array.set

@Composable
fun MainScreen(
    mainViewModel: MainViewModel,
    navController: NavController,
    token : String
){

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column {
                MainLogo()
                Spacer(modifier = Modifier.height(25.dp))
                ItemList(navController = navController, token = token, viewModel = mainViewModel)
            }
        }
        mainViewModel.fetchFiles(token)

    }
}

@Composable
fun MainLogo(){
    Text(
        text = "Cloud Castle",
        modifier = Modifier.fillMaxWidth(),
        color = Color.Black,
        fontWeight = FontWeight.ExtraBold,
        textAlign = TextAlign.Center
    )
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
            modifier = Modifier.align(CenterHorizontally)
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
                .data("${API_THUMBNAIL_URL}${fileItem.id}")
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
    viewModel: MainViewModel
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

    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp)
    ){
        items(filesList){
            Item(navController = navController, token = token, fileItem = it)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
    ){
        if(isLoading){
            CircularProgressIndicator(
                color = Color.Black
            )
        }
        if(resultError.isNotEmpty()){
            Failed(errorMessage = resultError) {
                viewModel.fetchFiles(token)
            }
        }
        if(filesList.isEmpty() && !isLoading){
            Text(text = "No data found", textAlign = TextAlign.Center)
        }
    }
}

