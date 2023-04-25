package com.bluecoder.cloudcastle.core.repos.files

import com.bluecoder.cloudcastle.core.data.FileItem
import kotlinx.coroutines.flow.Flow

interface FilesRepo {

    suspend fun getFiles(token : String,fileType : String) : Flow<Result<List<FileItem>>>

    suspend fun getFileById(token : String,id : Int) : Flow<Result<FileItem>>

    suspend fun deleteFileById(token: String,id : Int) : Flow<Result<String>>

    suspend fun deleteFiles(token: String,files : List<FileItem>) : Flow<Result<String>>
}