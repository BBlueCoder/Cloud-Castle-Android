package com.bluecoder.cloudcastle.data.repos

import com.bluecoder.cloudcastle.data.api.ServerAPI
import com.bluecoder.cloudcastle.data.pojoclasses.User
import com.bluecoder.cloudcastle.data.pojoclasses.UserJWT
import javax.inject.Inject

class UsersRepo @Inject constructor(private val serverAPI: ServerAPI) {

    suspend fun signup(user: User): Result<UserJWT> {
        return try {
            val resp = serverAPI.signup(user)
            if(!resp.isSuccessful){
                val errorMsg = resp.errorBody()?.string()
                //Log.e("API ERROR",""+errorMsg)
                throw Exception(errorMsg)
            }

            if(resp.body() == null)
                throw Exception("Response body is empty")

            Result.success(resp.body()!!)
        }catch (ex : Exception){
            Result.failure(ex)
        }
    }

    suspend fun login(user: User): Result<UserJWT> {
        return try {
            val resp = serverAPI.login(user)
            if(!resp.isSuccessful){
                val errorMsg = resp.errorBody()?.string()
                //Log.e("API ERROR",""+errorMsg)
                throw Exception(errorMsg)
            }

            if(resp.body() == null)
                throw Exception("Response body is empty")

            Result.success(resp.body()!!)
        }catch (ex : Exception){
            Result.failure(ex)
        }
    }


}