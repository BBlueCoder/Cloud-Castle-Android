package com.bluecoder.cloudcastle.ui.screens.display.components

import android.content.Context
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer

import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.PlayerView
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.theme.SmokeWhite
import com.bluecoder.cloudcastle.utils.Utils
import okhttp3.OkHttpClient

@Composable
fun VideoDisplay(
    lifecycleOwner: LifecycleOwner,
    context: Context,
    player: ExoPlayer,
    isAudio : Boolean,
    trackText : String,
    onBackClick : () -> Unit
) {


    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }

    var isBackButtonVisible by remember {
        mutableStateOf(false)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            player.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PlayerView(context).apply {
                    this.player = player
                    this.setControllerVisibilityListener(
                        PlayerView.ControllerVisibilityListener {controlVisibility ->
                            isBackButtonVisible = controlVisibility == View.VISIBLE
                    })
                }
            },
            update = {
                when(lifecycle){
                    Lifecycle.Event.ON_PAUSE -> {
                        it.onPause()
                        it.player?.pause()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                    }
                    else -> Unit
                }
            }
        )

        Box(modifier = Modifier
            .align(Alignment.TopStart)
            .padding(top = 30.dp)){
            AnimatedVisibility(visible = isBackButtonVisible) {
                IconButton(onClick = {
                    onBackClick()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "back", tint = Color.White)
                }
            }
        }

        if(isAudio){
            Box(modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)){
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(12.dp)
                            .background(SmokeWhite)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.music),
                            contentDescription = null,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .height(64.dp)
                                .width(64.dp)
                                .background(SmokeWhite)
                        )
                    }

                    Text(
                        text = trackText,
                        style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}