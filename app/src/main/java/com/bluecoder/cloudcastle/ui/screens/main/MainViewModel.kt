package com.bluecoder.cloudcastle.ui.screens.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluecoder.cloudcastle.core.repos.files.FilesRepo
import com.bluecoder.cloudcastle.ui.screens.main.data.FilesData
import com.bluecoder.cloudcastle.utils.Constants.FILES_TYPE_AUDIOS
import com.bluecoder.cloudcastle.utils.Constants.FILES_TYPE_IMAGES
import com.bluecoder.cloudcastle.utils.Constants.FILES_TYPE_OTHERS
import com.bluecoder.cloudcastle.utils.Constants.FILES_TYPE_VIDEOS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: FilesRepo): ViewModel() {

    var filesList = mutableStateOf<FilesData>(FilesData.Images(listOf()))
    var resultError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    fun fetchFiles(token : String,fileType : String){
        isLoading.value = true
        resultError.value = ""
        viewModelScope.launch {
            repo.getFiles(token,fileType.dropLast(1)).collectLatest {result ->
                isLoading.value = false
                if(result.isFailure){
                    resultError.value = result.exceptionOrNull()?.message?: ""
                    return@collectLatest
                }
                println("dataTag ******* ${result.getOrNull()?.size}")
                when(fileType){
                    FILES_TYPE_IMAGES -> {
                        println("dataTag ************** $fileType")
                        filesList.value = FilesData.Images(result.getOrNull()?: emptyList())
                    }

                    FILES_TYPE_VIDEOS -> {
                        println("dataTag ************** $fileType")
                        filesList.value = FilesData.Videos(result.getOrNull()?: emptyList())
                    }

                    FILES_TYPE_AUDIOS -> {
                        println("dataTag ************** $fileType")
                        filesList.value = FilesData.Audios(result.getOrNull()?: emptyList())
                    }

                    FILES_TYPE_OTHERS -> {
                        println("dataTag ************** $fileType")
                        filesList.value = FilesData.Others(result.getOrNull()?: emptyList())
                    }
                }
            }
        }
    }
}