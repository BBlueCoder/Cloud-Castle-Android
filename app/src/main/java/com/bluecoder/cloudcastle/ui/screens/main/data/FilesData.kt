package com.bluecoder.cloudcastle.ui.screens.main.data

import com.bluecoder.cloudcastle.data.pojoclasses.FileItem

sealed class FilesData{

    data class Images(
        val data : List<FileItem>
    ) : FilesData()

    data class Videos(
        val data : List<FileItem>
    ) : FilesData()

    data class Audios(
        val data : List<FileItem>
    ) : FilesData()

    data class Others(
        val data : List<FileItem>
    ) : FilesData()
}
