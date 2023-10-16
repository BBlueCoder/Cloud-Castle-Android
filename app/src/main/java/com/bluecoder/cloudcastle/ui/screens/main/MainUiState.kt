package com.bluecoder.cloudcastle.ui.screens.main

import com.bluecoder.cloudcastle.data.pojoclasses.FileItem

sealed interface MainUiState {
    object Loading : MainUiState
    object Error : MainUiState

    data class Success(
        val data : List<FileItem>
    ) : MainUiState
}