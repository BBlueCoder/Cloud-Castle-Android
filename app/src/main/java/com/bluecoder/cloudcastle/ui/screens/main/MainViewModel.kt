package com.bluecoder.cloudcastle.ui.screens.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluecoder.cloudcastle.core.data.FileItem
import com.bluecoder.cloudcastle.core.repos.files.FilesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: FilesRepo): ViewModel() {

    private val _filesList = MutableStateFlow<Result<List<FileItem>>>(Result.success(emptyList()))

    var filesList = mutableStateOf<List<FileItem>>(listOf())
    var resultError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    fun fetchFiles(token : String){
        isLoading.value = true
        resultError.value = ""
        viewModelScope.launch {
            repo.getFiles(token).collectLatest {result ->
                isLoading.value = false
                if(result.isFailure){
                    resultError.value = result.exceptionOrNull()?.message?: ""
                    return@collectLatest
                }
                filesList.value = result.getOrNull()?: emptyList()
            }
        }
    }
}