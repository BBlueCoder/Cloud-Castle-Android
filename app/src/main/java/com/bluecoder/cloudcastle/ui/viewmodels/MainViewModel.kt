package com.bluecoder.cloudcastle.ui.viewmodels

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
class MainViewModel @Inject constructor(private val repo: FilesRepo) : ViewModel(),MainViewModelInterface {

    override var filesList = mutableStateOf<FilesData>(FilesData.Images(listOf()))
    override var resultError = mutableStateOf("")
    override var isLoading = mutableStateOf(false)
    override var endReached = mutableStateOf(false)

    override var scrollPositions = mutableMapOf(
        FILES_TYPE_IMAGES to 0,
        FILES_TYPE_VIDEOS to 0,
        FILES_TYPE_AUDIOS to 0,
        FILES_TYPE_OTHERS to 0
    )

    override var scrollOffset = mutableMapOf(
        FILES_TYPE_IMAGES to 0,
        FILES_TYPE_VIDEOS to 0,
        FILES_TYPE_AUDIOS to 0,
        FILES_TYPE_OTHERS to 0
    )

    override fun fetchFiles(token: String, fileType: String) {
        isLoading.value = true
        resultError.value = ""
        viewModelScope.launch {
            repo.getFiles(token, fileType.dropLast(1)).collectLatest { result ->
                isLoading.value = false
                if (result.isFailure) {
                    resultError.value = result.exceptionOrNull()?.message ?: ""
                    return@collectLatest
                }
                when (fileType) {
                    FILES_TYPE_IMAGES -> {
                        filesList.value = FilesData.Images(result.getOrNull() ?: emptyList(),)
                    }

                    FILES_TYPE_VIDEOS -> {
                        filesList.value = FilesData.Videos(result.getOrNull() ?: emptyList())
                    }

                    FILES_TYPE_AUDIOS -> {
                        filesList.value = FilesData.Audios(result.getOrNull() ?: emptyList())
                    }

                    FILES_TYPE_OTHERS -> {
                        filesList.value = FilesData.Others(result.getOrNull() ?: emptyList())
                    }
                }
            }
        }
    }

    override fun updateScrollPosition(fileType: String, position : Int, offset : Int) {
        scrollPositions[fileType] = position
        scrollOffset[fileType] = offset
    }


}