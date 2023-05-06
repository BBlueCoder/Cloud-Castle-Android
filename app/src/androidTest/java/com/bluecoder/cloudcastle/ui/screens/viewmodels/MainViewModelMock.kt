package com.bluecoder.cloudcastle.ui.screens.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.bluecoder.cloudcastle.core.data.FileItem
import com.bluecoder.cloudcastle.ui.screens.main.data.FilesData
import com.bluecoder.cloudcastle.ui.viewmodels.MainViewModelInterface
import com.bluecoder.cloudcastle.utils.Constants

class MainViewModelMock : MainViewModelInterface {

    private val fakeContentLength = 1024L
    private val fakeDate = System.currentTimeMillis()
    private val thumbnailPlaceHolderUrl = "https://placehold.co/400"

    private var fakeFilesDataImages = listOf(
        FileItem(0,"fakeImg","fakeImg","image/png",fakeContentLength,fakeDate,null),
        FileItem(1,"fakeImg","fakeImg","image/png",fakeContentLength,fakeDate,null),
        FileItem(2,"fakeImg","fakeImg","image/png",fakeContentLength,fakeDate,null),
        FileItem(3,"fakeImg","fakeImg","image/png",fakeContentLength,fakeDate,null),
        FileItem(4,"fakeImg","fakeImg","image/png",fakeContentLength,fakeDate,null)
    )

    override var filesList: MutableState<FilesData> = mutableStateOf(FilesData.Images(listOf()))

    override var resultError = mutableStateOf("")

    override var isLoading = mutableStateOf(false)

    override var endReached = mutableStateOf(false)

    override var scrollPositions = mutableMapOf(
        Constants.FILES_TYPE_IMAGES to 0,
        Constants.FILES_TYPE_VIDEOS to 0,
        Constants.FILES_TYPE_AUDIOS to 0,
        Constants.FILES_TYPE_OTHERS to 0
    )

    override var scrollOffset = mutableMapOf(
        Constants.FILES_TYPE_IMAGES to 0,
        Constants.FILES_TYPE_VIDEOS to 0,
        Constants.FILES_TYPE_AUDIOS to 0,
        Constants.FILES_TYPE_OTHERS to 0
    )

    override fun fetchFiles(token: String, fileType: String) {
        filesList.value = FilesData.Images(fakeFilesDataImages)
    }

    override fun updateScrollPosition(fileType: String, position: Int, offset: Int) {
        scrollPositions[fileType] = position
        scrollOffset[fileType] = offset
    }
}