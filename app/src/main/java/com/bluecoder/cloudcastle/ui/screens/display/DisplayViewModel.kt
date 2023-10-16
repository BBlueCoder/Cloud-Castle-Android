package com.bluecoder.cloudcastle.ui.screens.display

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.bluecoder.cloudcastle.data.pojoclasses.FileItem
import com.bluecoder.cloudcastle.data.repos.FilesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel @Inject constructor(
    private val repo: FilesRepo,
    val exoPlayer: ExoPlayer
) : ViewModel() {

    private val _itemUiState = MutableStateFlow<Result<FileItem>?>(null)
    val itemUiState: StateFlow<Result<FileItem>?> = _itemUiState

    fun fetchItem(fileId : Int){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getFileById(fileId)
            _itemUiState.value = result
        }
    }
}