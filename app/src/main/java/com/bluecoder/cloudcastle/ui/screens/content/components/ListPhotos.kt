package com.bluecoder.cloudcastle.ui.screens.content.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.data.pojoclasses.FileItem
import com.bluecoder.cloudcastle.ui.theme.Black
import com.bluecoder.cloudcastle.utils.Constants
import com.bluecoder.cloudcastle.utils.Utils

@Composable
fun ListPhotos(
    data : List<FileItem>,
    isSelectionEnabled : Boolean,
    onItemClick: (fileItem : FileItem) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        horizontalArrangement = Arrangement.spacedBy(1.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ){
        items(data) {fileItem ->

            var isSelected by remember {
                mutableStateOf(false)
            }

            if(!isSelectionEnabled)
                isSelected = false

            Box(
                modifier = Modifier
                    .height(70.dp)
                    .clickable {
                        if (isSelectionEnabled)
                            isSelected = !isSelected
                        onItemClick(fileItem)
                    }
            ){
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
                if(fileItem.fileType.contains("video") && fileItem.duration != null){
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 5.dp, end = 5.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Black.copy(alpha = 0.5F))
                    ){
                        Text(
                            text = Utils.formatDuration(fileItem.duration),
                            color = Color.White,
                            fontWeight = FontWeight.Thin,
                            fontSize = 8.sp,
                            modifier = Modifier.padding(
                                start = 4.dp,
                                end = 4.dp,
                                top = 2.dp,
                                bottom = 2.dp
                            )
                        )
                    }
                }
                if(isSelectionEnabled && isSelected){
                    Box(modifier = Modifier.fillMaxSize().background(Black.copy(alpha = 0.5f)))
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ){
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color.Blue.copy(alpha = 0.8f))
                    }
                }
            }
        }
    }
}