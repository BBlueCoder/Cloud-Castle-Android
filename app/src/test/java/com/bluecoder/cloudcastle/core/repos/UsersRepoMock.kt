package com.bluecoder.cloudcastle.core.repos

import com.bluecoder.cloudcastle.core.data.User
import com.bluecoder.cloudcastle.core.data.UserJWT
import com.bluecoder.cloudcastle.core.repos.users.UsersRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UsersRepoMock : UsersRepo {

    private val userTest = User("user","123")

    private val validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODE0MTgzMjEsImRhdGEiOnsiaWQiOjMsInVzZXJuYW1lIjoidXNlcjIifSwiaWF0IjoxNjgxNDE2MjIxfQ.FrF6qw730u1SMAIWy0PsY4YzxJG-a-UWPBpwQuvO0_U"

    override suspend fun signup(user: User): Flow<Result<UserJWT>> = flow {
        if(user.username == userTest.username) {
            emit(Result.failure(Exception("Username is duplicated, please change your username")))
            return@flow
        }

        emit(Result.success(UserJWT(validToken)))
    }

    override suspend fun login(user: User): Flow<Result<UserJWT>> = flow {
        if(user.username != userTest.username){
            emit(Result.failure(Exception("User not found, please enter a valid username")))
            return@flow
        }

        if(user.password != userTest.password){
            emit(Result.failure(Exception("Password is incorrect")))
            return@flow
        }

        emit(Result.success(UserJWT(validToken)))
    }
}