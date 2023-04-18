package com.bluecoder.cloudcastle.core.data

import com.bluecoder.cloudcastle.utils.FileType

data class FileItem(
    val id : Int,
    val originName : String,
    val savedName : String,
    val fileType : String,
    val contentLength : Long,
    val dateInMillis : Long,
    val duration : Double?
)
