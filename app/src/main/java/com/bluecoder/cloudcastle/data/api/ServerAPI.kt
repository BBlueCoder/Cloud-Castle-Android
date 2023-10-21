package com.bluecoder.cloudcastle.data.api

import com.bluecoder.cloudcastle.data.pojoclasses.FileItem
import com.bluecoder.cloudcastle.data.pojoclasses.FilesCount
import com.bluecoder.cloudcastle.data.pojoclasses.User
import com.bluecoder.cloudcastle.data.pojoclasses.UserJWT
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerAPI {

    @POST("users/signup")
    suspend fun signup(@Body user: User): Response<UserJWT>

    @POST("users/login")
    suspend fun login(@Body user: User): Response<UserJWT>

    @GET("files")
    suspend fun getFiles(
        @Query("file_type") fileType : String = "image"
    ): Response<List<FileItem>>

    @GET("files/count")
    suspend fun getFilesCountByType(
        @Query("file_type") fileType : String
    ): Response<FilesCount>

    @GET("files/metadata/{id}")
    suspend fun getFileById(
        @Path("id") id: Int
    ) : Response<FileItem>

    @Multipart
    @POST("files")
    suspend fun addFiles(
        @Part files : List<MultipartBody.Part>
    ) : Response<List<FileItem>>

    @DELETE("files/{id}")
    suspend fun deleteFileById(
        @Path("id") id: Int
    ) : Response<String>

    @HTTP(method = "DELETE", path = "files",hasBody = true)
    suspend fun deleteFiles(
        @Body files : List<FileItem>
    ) : Response<Any>
}