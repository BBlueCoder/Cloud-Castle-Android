package com.bluecoder.cloudcastle.ui.screens.content.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.data.pojoclasses.FileItem
import com.bluecoder.cloudcastle.ui.theme.Black
import com.bluecoder.cloudcastle.utils.Utils

@Composable
fun ListDocuments(
    data : List<FileItem>,
    isSelectionEnabled : Boolean,
    onItemClick: (fileItem : FileItem) -> Unit
) {
    val context = LocalContext.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        items(data) { fileItem ->

            var isSelected by remember {
                mutableStateOf(false)
            }

            if(!isSelectionEnabled)
                isSelected = false

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (isSelectionEnabled)
                            isSelected = !isSelected
                        onItemClick(fileItem)
                    }
                    .semantics {
                        testTag = context.resources.getString(R.string.content_item)
                    }
            ){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.height(66.dp).width(68.dp)){
                        Image(
                            painter = painterResource(Utils.getThumbnailForFiles(fileItem.fileType)),
                            contentDescription = fileItem.originName,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        if(isSelectionEnabled && isSelected){
                            Box(modifier = Modifier
                                .fillMaxSize()
                            ){
                                Box(modifier = Modifier.align(Alignment.TopEnd)){
                                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color.Blue.copy(alpha = 0.8f))
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = fileItem.originName,
                        fontWeight = FontWeight.Thin,
                        textAlign = TextAlign.Center,
                        fontSize = 8.sp,
                        color = Black,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }


            }
        }
    }
}