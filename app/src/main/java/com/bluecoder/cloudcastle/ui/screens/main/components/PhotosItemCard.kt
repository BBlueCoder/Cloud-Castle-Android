package com.bluecoder.cloudcastle.ui.screens.main.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.data.pojoclasses.FileItem
import com.bluecoder.cloudcastle.ui.theme.SmokeWhite
import com.bluecoder.cloudcastle.utils.Constants

private const val MAX_HEIGHT = 117
private const val MAX_WIDTH = 123
private const val MIN_HEIGHT = 101
private const val MIN_WIDTH = 84

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosItemCard(
    onItemClick: () -> Unit,
    fileItem: FileItem,
    isBig: Boolean
) {
    Box(modifier = Modifier.height(MAX_HEIGHT.dp)){
        Card(
            modifier = Modifier
                .animateContentSize()
                .height(if (isBig) MAX_HEIGHT.dp else MIN_HEIGHT.dp)
                .width(if (isBig) MAX_WIDTH.dp else MIN_WIDTH.dp),
            onClick = {
                onItemClick()
            },
            shape = RoundedCornerShape(10.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("${Constants.API_THUMBNAIL_URL}${fileItem.id}")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.picture_placeholder),
                    modifier = Modifier.fillMaxSize()
                )
                if(fileItem.fileType.contains("video")){
                    Box(modifier = Modifier.align(Alignment.Center)){
                        Icon(
                            Icons.Default.PlayArrow    ,
                            contentDescription =null,
                            tint = SmokeWhite
                        )
                    }
                }
            }
        }
    }
}
