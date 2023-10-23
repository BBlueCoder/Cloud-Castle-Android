package com.bluecoder.cloudcastle.data.repos

import com.bluecoder.cloudcastle.utils.CacheManager
import com.bluecoder.cloudcastle.data.api.ServerAPI
import com.bluecoder.cloudcastle.data.pojoclasses.FileItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import javax.inject.Inject

class FilesRepo @Inject constructor(
    private val serverAPI: ServerAPI,
    private val cacheManager: CacheManager
) {

    companion object {
        const val GET_FILES_CALL = "getFiles/"
        const val GET_FILES_COUNT = "getFilesCountByType/"
    }

    private suspend fun <T> handleWork(
        apiCall: suspend () -> retrofit2.Response<T>
    ): T {
        val resp = apiCall()

        if (!resp.isSuccessful) {
            val errorMsg = resp.errorBody()?.string()
            throw Exception(errorMsg)
        }

        if (resp.body() == null)
            throw Exception("Response body is empty")

        return resp.body()!!
    }

    fun getFiles(fileType: String): Flow<Result<List<FileItem>>> = flow {
        try {
            val responseFromCache = cacheManager.getCachedResponse("$GET_FILES_CALL$fileType")
            responseFromCache?.let {
                emit(Result.success(it))
                return@flow
            }

            val result = handleWork {
                serverAPI.getFiles(fileType)
            }

            emit(Result.success(result))
            cacheManager.cacheResponse("$GET_FILES_CALL$fileType", result)
        } catch (ex: Exception) {
            emit(Result.failure(ex))
        }
    }.flowOn(Dispatchers.IO)

    fun getFilesCountByType(
        fileType: String
    ) = flow {
        try {
            val countFromCache = cacheManager.getSingleCachedValue("$GET_FILES_COUNT$fileType")
            countFromCache?.let {
                emit(Result.success(it))
                return@flow
            }
            val result = handleWork {
                serverAPI.getFilesCountByType(fileType)
            }

            emit(Result.success(result))
            cacheManager.cacheResponse("$GET_FILES_COUNT$fileType", result.count)
        } catch (ex: Exception) {
            emit(Result.failure(ex))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getFileById(id: Int): Result<FileItem> {
        return try {
            val fileFromCache = cacheManager.getFileItemFromCache(id)
            fileFromCache?.let {
                return Result.success(it)
            }

            val result = handleWork {
                serverAPI.getFileById(id)
            }

            Result.success(result)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun addFiles(files: List<MultipartBody.Part>): Result<String> {
        return try {

            handleWork {
                serverAPI.addFiles(files)
            }

            cacheManager.clearAllCache()
            Result.success("")
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun deleteFileById(id: Int): Result<String> {
        return try {
            val result = handleWork {
                serverAPI.deleteFileById(id)
            }

            cacheManager.clearAllCache()
            Result.success(result)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun deleteFiles(
        files: List<FileItem>
    ): Result<String> {
        return try {
            handleWork {
                serverAPI.deleteFiles(files)
            }

            cacheManager.clearAllCache()
            Result.success("")
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }


}