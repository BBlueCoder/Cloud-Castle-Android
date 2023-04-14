package com.bluecoder.cloudcastle.core.repos.users

import com.bluecoder.cloudcastle.core.data.User
import com.bluecoder.cloudcastle.core.data.UserJWT
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

interface UsersRepo {

    suspend fun signup(user : User) : Flow<Result<UserJWT>>

    suspend fun login(user : User) : Flow<Result<UserJWT>>
}