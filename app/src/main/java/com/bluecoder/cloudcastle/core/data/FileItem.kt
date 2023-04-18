package com.bluecoder.cloudcastle.core.data


data class FileItem(
    val id : Int,
    val originName : String,
    val savedName : String,
    val fileType : String,
    val contentLength : Long,
    val dateInMillis : Long,
    val duration : Double?
)
