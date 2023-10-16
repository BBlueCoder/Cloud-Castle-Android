package com.bluecoder.cloudcastle.ui.screens.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.screens.main.MainUiState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PhotosSection(
    photosUiState: MainUiState,
    photosCount: Int,
    videosCount: Int,
    onSeeAllClick: () -> Unit
) {
    val isPhotosLoading = photosUiState is MainUiState.Loading
    val isPhotosLoadingFailed = photosUiState is MainUiState.Error

    Category(
        R.drawable.photos,
        "Photos",
        "$photosCount Photos, $videosCount Videos"
    ) {
        onSeeAllClick()
    }

    if(!isPhotosLoading && !isPhotosLoadingFailed){
        val data = (photosUiState as MainUiState.Success).data

        println("********************************************** data $data")

        val listState = rememberLazyListState()

        var firstVisibleItemIndex by remember {
            mutableIntStateOf(0)
        }

        LaunchedEffect(listState) {
            snapshotFlow {
                listState.firstVisibleItemScrollOffset
            }
                .collectLatest {offset ->
                    val itemSize = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
                    firstVisibleItemIndex = listState.firstVisibleItemIndex
                    if(offset >= itemSize/2)
                        firstVisibleItemIndex++
                }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            state = listState
        ){
            itemsIndexed(data){ index, item ->

                val isBig = index == firstVisibleItemIndex

                PhotosItemCard(
                    onItemClick = {
                    },
                    fileItem = item,
                    isBig = isBig
                )
            }
        }
    }
}