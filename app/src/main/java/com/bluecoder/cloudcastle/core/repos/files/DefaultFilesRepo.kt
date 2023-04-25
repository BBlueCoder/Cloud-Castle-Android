package com.bluecoder.cloudcastle.core.repos.files

import android.util.Log
import com.bluecoder.cloudcastle.core.api.ServerAPI
import com.bluecoder.cloudcastle.core.data.FileItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultFilesRepo @Inject constructor(private val serverAPI: ServerAPI) : FilesRepo {

    override suspend fun getFiles(token : String,fileType : String): Flow<Result<List<FileItem>>> = flow {
        try {
            val resp = serverAPI.getFiles(token,fileType)

            if(!resp.isSuccessful){
                val errorMsg = resp.errorBody()?.string()
                //Log.e("API ERROR",""+errorMsg)
                throw Exception(errorMsg)
            }

            if(resp.body() == null)
                throw Exception("Response body is empty")

            emit(Result.success(resp.body()!!))
        }catch (ex : Exception){
            emit(Result.failure(ex))
        }
    }

    override suspend fun getFileById(token : String,id: Int): Flow<Result<FileItem>> = flow {
        try {
            val resp = serverAPI.getFileById(token,id)

            if(!resp.isSuccessful){
                val errorMsg = resp.errorBody()?.string()
                //Log.e("API ERROR",""+errorMsg)
                throw Exception(errorMsg)
            }

            if(resp.body() == null)
                throw Exception("Response body is empty")

            emit(Result.success(resp.body()!!))
        }catch (ex : Exception){
            emit(Result.failure(ex))
        }
    }

    override suspend fun deleteFileById(token: String, id: Int): Flow<Result<String>> = flow {
        try {
            val resp = serverAPI.deleteFileById(token,id)

            if(!resp.isSuccessful){
                val errorMsg = resp.errorBody()?.string()
                //Log.e("API ERROR",""+errorMsg)
                throw Exception(errorMsg)
            }

            if(resp.body() == null)
                throw Exception("Response body is empty")

            emit(Result.success(resp.body()!!))
        }catch (ex : Exception){
            emit(Result.failure(ex))
        }
    }

    override suspend fun deleteFiles(
        token: String,
        files: List<FileItem>
    ): Flow<Result<String>>  = flow {
        try {
            val resp = serverAPI.deleteFiles(token, files)

            if(!resp.isSuccessful){
                val errorMsg = resp.errorBody()?.string()
                //Log.e("API ERROR",""+errorMsg)
                throw Exception(errorMsg)
            }

            if(resp.body() == null)
                throw Exception("Response body is empty")

            emit(Result.success(resp.body()!!))
        }catch (ex : Exception){
            emit(Result.failure(ex))
        }
    }


}