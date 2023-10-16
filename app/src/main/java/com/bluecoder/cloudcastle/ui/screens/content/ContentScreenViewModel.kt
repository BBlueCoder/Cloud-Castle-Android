package com.bluecoder.cloudcastle.ui.screens.content

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluecoder.cloudcastle.data.pojoclasses.FileItem
import com.bluecoder.cloudcastle.data.repos.FilesRepo
import com.bluecoder.cloudcastle.ui.screens.ScreenArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentScreenViewModel @Inject constructor(
    private val repo: FilesRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val fileType: String = checkNotNull(savedStateHandle[ScreenArgs.FileType.name])

    private val selectedItems = mutableListOf<FileItem>()

    var selectedItemsCount = MutableStateFlow(0)

    private val _contentState = MutableStateFlow<ContentsUiState>(ContentsUiState.Loading)
    val contentsState: StateFlow<ContentsUiState> = _contentState

    init {
        fetchFiles()
    }

    fun fetchFiles(){
        viewModelScope.launch {
            repo.getFiles(fileType).collect{
                it.onSuccess {data ->
                    _contentState.value = ContentsUiState.Success(data)
                }
                it.onFailure {
                    _contentState.value = ContentsUiState.Error
                }
            }
        }
    }
    fun deleteFiles(){
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.deleteFiles(selectedItems)
            println("******************************************* result $result")
            if(result.isSuccess)
                fetchFiles()
        }
    }

    fun toggleSelection(fileItem: FileItem) {
        if (!selectedItems.contains(fileItem))
            selectedItems.add(fileItem)
         else
            selectedItems.remove(fileItem)
        selectedItemsCount.value = selectedItems.size
    }

    fun unselectAllItems() {
        selectedItems.clear()
        selectedItemsCount.value = 0
    }
}