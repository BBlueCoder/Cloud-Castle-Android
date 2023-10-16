package com.bluecoder.cloudcastle.ui.screens.content

import com.bluecoder.cloudcastle.data.pojoclasses.FileItem

sealed interface ContentsUiState {
    object Loading : ContentsUiState
    object Error : ContentsUiState

    data class Success(
        val content : List<FileItem>
    ) : ContentsUiState
}