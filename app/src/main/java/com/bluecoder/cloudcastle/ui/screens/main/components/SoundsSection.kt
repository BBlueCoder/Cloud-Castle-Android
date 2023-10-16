package com.bluecoder.cloudcastle.ui.screens.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.screens.main.MainUiState
import com.bluecoder.cloudcastle.ui.theme.Black
import com.bluecoder.cloudcastle.utils.Utils

@Composable
fun SoundsSection(
    soundsUiState: MainUiState,
    soundsCount: Int,
    onSeeAllClick: () -> Unit
) {
    val isSoundsLoading = soundsUiState is MainUiState.Loading
    val isSoundsLoadingFailed = soundsUiState is MainUiState.Error

    Category(
        R.drawable.ic_sound_category,
        "Sounds",
        "$soundsCount tracks"
    ) {
        onSeeAllClick()
    }

    if (!isSoundsLoading && !isSoundsLoadingFailed) {
        val data = (soundsUiState as MainUiState.Success).data

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(data) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(68.dp)
                ) {
                    Image(
                        painter = painterResource(Utils.getThumbnailForFiles(it.fileType)),
                        contentDescription = it.originName,
                        alignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(66.dp)
                    )
                    Text(
                        text = it.originName,
                        fontWeight = FontWeight.Thin,
                        fontSize = 8.sp,
                        maxLines = 2,
                        modifier = Modifier.padding(vertical = 3.dp, horizontal = 6.dp),
                        overflow = TextOverflow.Ellipsis,
                        color = Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}