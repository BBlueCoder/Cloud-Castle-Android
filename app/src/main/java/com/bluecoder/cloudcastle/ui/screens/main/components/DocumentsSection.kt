package com.bluecoder.cloudcastle.ui.screens.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.ui.screens.main.MainUiState
import com.bluecoder.cloudcastle.utils.Utils

@Composable
fun DocumentsSection(
    documentsUiState: MainUiState,
    documentsCount: Int,
    onSeeAllClick: () -> Unit
) {
    val isDocumentsLoading = documentsUiState is MainUiState.Loading
    val isDocumentsLoadingFailed = documentsUiState is MainUiState.Error

    Category(
        R.drawable.documents,
        "Documents",
        "$documentsCount files"
    ) {
        onSeeAllClick()
    }

    if (!isDocumentsLoading && !isDocumentsLoadingFailed) {
        val data = (documentsUiState as MainUiState.Success).data

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(data) {
                Image(
                    painter = painterResource(Utils.getThumbnailForFiles(it.fileType)),
                    contentDescription = it.originName,
                    alignment = Alignment.Center,
                    modifier = Modifier.height(66.dp).width(68.dp)
                )
            }
        }
    }
}

