package com.bluecoder.cloudcastle.ui.screens.main

import androidx.compose.runtime.MutableState
import com.bluecoder.cloudcastle.ui.screens.main.data.FilesData
import kotlinx.coroutines.flow.MutableStateFlow

interface MainViewModelInterface{

    var filesList : MutableState<FilesData>
    var resultError : MutableState<String>
    var isLoading : MutableState<Boolean>
    var endReached : MutableState<Boolean>

    var scrollPositions : MutableMap<String,Int>
    var scrollOffset : MutableMap<String,Int>

    fun fetchFiles(token : String,fileType : String)
    fun updateScrollPosition(fileType: String,position : Int,offset : Int)
}