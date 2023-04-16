package com.bluecoder.cloudcastle.core.api

import com.bluecoder.cloudcastle.core.data.User
import com.bluecoder.cloudcastle.core.data.UserJWT
import retrofit2.Response
import retrofit2.http.POST

interface ServerAPI {

    @POST("users/signup")
    suspend fun signup(user: User) : Response<UserJWT>

    @POST("users/login")
    suspend fun login(user : User) : Response<UserJWT>
}