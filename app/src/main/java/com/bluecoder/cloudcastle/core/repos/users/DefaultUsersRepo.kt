package com.bluecoder.cloudcastle.core.repos.users

import android.util.Log
import com.bluecoder.cloudcastle.core.api.ServerAPI
import com.bluecoder.cloudcastle.core.data.User
import com.bluecoder.cloudcastle.core.data.UserJWT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

class DefaultUsersRepo @Inject constructor(private val serverAPI: ServerAPI) : UsersRepo {

    override suspend fun signup(user: User): Flow<Result<UserJWT>> = flow {
        try {
            val resp = serverAPI.signup(user)
            if(!resp.isSuccessful){
                val errorMsg = resp.errorBody()?.string()
                Log.e("API ERROR",""+errorMsg)
                throw Exception(errorMsg)
            }

            if(resp.body() == null)
                throw Exception("Response body is empty")

            emit(Result.success(resp.body()!!))
        }catch (ex : Exception){
            emit(Result.failure(ex))
        }
    }

    override suspend fun login(user: User): Flow<Result<UserJWT>> = flow {
        try {
            val resp = serverAPI.login(user)
            if(!resp.isSuccessful){
                val errorMsg = resp.errorBody()?.string()
                Log.e("API ERROR",""+errorMsg)
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