package com.bluecoder.cloudcastle.core.api

import com.bluecoder.cloudcastle.core.data.FileItem
import com.bluecoder.cloudcastle.core.data.User
import com.bluecoder.cloudcastle.core.data.UserJWT
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ServerAPI {

    @POST("users/signup")
    suspend fun signup(@Body user: User): Response<UserJWT>

    @POST("users/login")
    suspend fun login(@Body user: User): Response<UserJWT>

    @GET("files")
    suspend fun getFiles(@Header("authentication") token: String): Response<List<FileItem>>

    @GET("files/metadata/{id}")
    suspend fun getFileById(
        @Header("authentication") token: String,
        @Path("id") id: Int
    ) : Response<FileItem>

    @DELETE("files/{id}")
    suspend fun deleteFileById(
        @Header("authentication") token: String,
        @Path("id") id: Int
    ) : Response<String>

    @HTTP(method = "DELETE", path = "files",hasBody = true)
    suspend fun deleteFiles(
        @Header("authentication") token: String,
        @Body files : List<FileItem>
    ) : Response<String>
}