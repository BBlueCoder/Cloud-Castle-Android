package com.bluecoder.cloudcastle.ui.screens.display

import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.screens.content.components.findActivity
import com.bluecoder.cloudcastle.ui.screens.display.components.VideoDisplay
import com.bluecoder.cloudcastle.ui.theme.Black
import com.bluecoder.cloudcastle.utils.Constants
import okhttp3.Headers

@Composable
fun DisplayScreen(
    fileId : Int,
    onBackPressed : () -> Unit,
    viewModel: DisplayViewModel = hiltViewModel()
) {

    viewModel.fetchItem(fileId)
    val itemUiState = viewModel.itemUiState.collectAsStateWithLifecycle()

    val isItemLoaded = itemUiState.value != null && itemUiState.value!!.isSuccess
    val context = LocalContext.current
    (context.findActivity() as ComponentActivity).enableEdgeToEdge()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black),

    ){
        if(isItemLoaded){
            val fileItem = itemUiState.value!!.getOrNull()!!

            if(fileItem.fileType.contains("image")){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${Constants.API_THUMBNAIL_URL}${fileItem.id}")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Image",
                    placeholder = painterResource(id = R.drawable.picture_placeholder),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
            }

            if(fileItem.fileType.contains("video") || fileItem.fileType.contains("audio")){
                val mediaItem = MediaItem.fromUri("${Constants.API_FILES_URL}${fileItem.id}")
                VideoDisplay(
                    lifecycleOwner = LocalLifecycleOwner.current,
                    context = LocalContext.current,
                    player = viewModel.exoPlayer.apply {
                        setMediaItem(mediaItem)
                        prepare()
                        playWhenReady = true
                    },
                    isAudio = fileItem.fileType.contains("audio"),
                    trackText = fileItem.originName,
                    onBackClick = {
                        onBackPressed()
                    }
                )
            }
        }
    }
}