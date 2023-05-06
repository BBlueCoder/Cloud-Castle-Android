package com.bluecoder.cloudcastle.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.core.data.FileItem
import com.bluecoder.cloudcastle.ui.screens.main.data.FilesData
import com.bluecoder.cloudcastle.ui.viewmodels.MainViewModel
import com.bluecoder.cloudcastle.ui.viewmodels.MainViewModelInterface
import com.bluecoder.cloudcastle.utils.Constants
import com.bluecoder.cloudcastle.utils.Constants.FILES_TYPE_AUDIOS
import com.bluecoder.cloudcastle.utils.Constants.FILES_TYPE_IMAGES
import com.bluecoder.cloudcastle.utils.Constants.FILES_TYPE_OTHERS
import com.bluecoder.cloudcastle.utils.Constants.FILES_TYPE_VIDEOS
import com.bluecoder.cloudcastle.utils.Utils
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import okhttp3.Headers

@Composable
fun DisplayGrid(
    navController: NavController,
    viewModel: MainViewModelInterface,
    token: String,
    screen: String
) {
    ItemList(navController = navController, token = token, viewModel = viewModel, screen = screen)
}

@Composable
fun Failed(
    errorMessage: String,
    onRetry: () -> Unit
) {
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
    fileItem: FileItem
) {
    Box(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
    ) {
        if (fileItem.fileType.contains("image") || fileItem.fileType.contains("video")) {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${Constants.API_THUMBNAIL_URL}${fileItem.id}")
                        .headers(Headers.Builder().apply {
                            add("Authentication", token)
                        }.build())
                        .crossfade(true)
                        .build(),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.picture_placeholder)
                )
                if (fileItem.fileType.contains("video") && fileItem.duration != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .clip(RoundedCornerShape(topStart = 5.dp))
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = Utils.formatDuration(fileItem.duration),
                            color = Color.White,
                            fontSize = 8.sp,
                            modifier = Modifier.padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 3.dp,
                                bottom = 3.dp
                            )
                        )
                    }
                }
            }

        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(Utils.getThumbnailForFiles(fileItem.fileType)),
                    contentDescription = fileItem.originName,
                    Modifier.weight(0.7f),
                    alignment = Alignment.Center
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = Utils.shortenFilenameIdNeeded(fileItem.originName),
                    fontSize = 8.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(0.3f)
                )
            }
        }

    }
}


@OptIn(FlowPreview::class)
@Composable
fun ItemList(
    navController: NavController,
    token: String,
    viewModel: MainViewModelInterface,
    screen: String
) {
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
    val scrollPosition = viewModel.scrollPositions[screen]
    val scrollOffset = viewModel.scrollOffset[screen]

    val lazyVerticalState = rememberLazyGridState()

    LaunchedEffect(scrollOffset) {
        if (data.isNotEmpty()) {
            snapshotFlow { lazyVerticalState.layoutInfo }
                .filter { it.visibleItemsInfo.isNotEmpty() }
                .first()

            lazyVerticalState.animateScrollToItem(scrollPosition!!, scrollOffset!!)
        }
    }

    LaunchedEffect(lazyVerticalState) {
        snapshotFlow {
            lazyVerticalState.firstVisibleItemScrollOffset
        }
            .debounce(600L)
            .collectLatest { offset ->
                if(checkIfScreenMatchDataType(screen,filesList)){
                    viewModel.updateScrollPosition(
                        screen,
                        lazyVerticalState.firstVisibleItemIndex,
                        offset
                    )
                }
            }
    }

    BoxWithConstraints {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(100.dp),
            horizontalArrangement = Arrangement.Center,
            state = lazyVerticalState
        ) {
            items(data) {
                Item(navController = navController, token = token, fileItem = it)
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.Black
            )
        }
        if (resultError.isNotEmpty()) {
            Failed(errorMessage = resultError) {
                viewModel.fetchFiles(token, screen)
            }
        }
        if (data.isEmpty() && !isLoading) {
            Text(text = "No data found", textAlign = TextAlign.Center)
        }
    }
}

fun getItemsFromFilesData(data: FilesData): List<FileItem> {
    return when (data) {
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

/*  When navigating between different screens
    the lazyVerticalGrid state launched effect
    keeps updating the previous screen scroll position
    to fix that, it will check if the screen matches the current type of data
*/
private fun checkIfScreenMatchDataType(screen: String,filesData: FilesData): Boolean{
    if(screen == FILES_TYPE_IMAGES && filesData is FilesData.Images)
        return true

    if(screen == FILES_TYPE_VIDEOS && filesData is FilesData.Videos)
        return true

    if(screen == FILES_TYPE_AUDIOS && filesData is FilesData.Audios)
        return true

    if(screen == FILES_TYPE_OTHERS && filesData is FilesData.Others)
        return true

    return false
}