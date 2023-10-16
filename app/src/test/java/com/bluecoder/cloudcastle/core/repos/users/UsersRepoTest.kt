package com.bluecoder.cloudcastle.core.repos.users

import com.bluecoder.cloudcastle.data.api.ServerAPI
import com.bluecoder.cloudcastle.data.pojoclasses.User
import com.bluecoder.cloudcastle.data.pojoclasses.UserJWT
import com.bluecoder.cloudcastle.core.repos.buildResponse
import com.bluecoder.cloudcastle.data.repos.UsersRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UsersRepoTest {

    private lateinit var serverAPI: ServerAPI

    private lateinit var usersRepo: UsersRepo

    private val validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODE0MTgzMjEsImRhdGEiOnsiaWQiOjMsInVzZXJuYW1lIjoidXNlcjIifSwiaWF0IjoxNjgxNDE2MjIxfQ.FrF6qw730u1SMAIWy0PsY4YzxJG-a-UWPBpwQuvO0_U"
    private val fakeValidUser = User("user","123")
    private val fakeInvalidUser = User("invalid_username","123")
    private val fakeDuplicateUser = User("duplicate_user","123")
    private val fakeUserWithInvalidPassword = User("user","invalid_password")

    private val msgForInvalidUser = "User not found, please enter a valid username"
    private val msgForDuplicateUser = "Username is duplicated, please change your username"
    private val msgForInvalidPassword = "Password is incorrect"
    @Before
    fun setup(){
        serverAPI = mockk()
        stubApi()
        usersRepo = UsersRepo(serverAPI)
    }

    private fun stubApi(){
        coEvery{ serverAPI.signup(fakeValidUser) } returns buildResponse(true, UserJWT(validToken))
        coEvery{ serverAPI.login(fakeValidUser) } returns buildResponse(true, UserJWT(validToken))

        coEvery{ serverAPI.signup(fakeInvalidUser) } returns buildResponse(isResponseSuccessful = false, errorResponseMessage = msgForInvalidUser)
        coEvery{ serverAPI.login(fakeInvalidUser) } returns buildResponse(isResponseSuccessful = false, errorResponseMessage = msgForInvalidUser)

        coEvery{ serverAPI.signup(fakeDuplicateUser) } returns buildResponse(isResponseSuccessful = false, errorResponseMessage = msgForDuplicateUser)

        coEvery { serverAPI.login(fakeUserWithInvalidPassword) } returns buildResponse(isResponseSuccessful = false, errorResponseMessage = msgForInvalidPassword)
    }

    @Test
    fun `signup with valid user`(): Unit = runBlocking {
        val results = usersRepo.signup(fakeValidUser)
        results.let{
            assertTrue(it.isSuccess)
            assertEquals(validToken,it.getOrNull()?.token)
        }
    }

    @Test
    fun `signup with duplicate user`() : Unit = runBlocking {
        val results = usersRepo.signup(fakeDuplicateUser)
        results.let{
            assertTrue(it.isFailure)
            assertEquals(msgForDuplicateUser,it.exceptionOrNull()?.message)
        }
    }

    @Test
    fun `login with valid user`() : Unit = runBlocking {
        val results = usersRepo.login(fakeValidUser)
        results.let{
            assertTrue(it.isSuccess)
            assertEquals(validToken,it.getOrNull()?.token)
        }
    }

    @Test
    fun `login with invalid username`() : Unit = runBlocking {
        val results = usersRepo.login(fakeInvalidUser)
        results.let{
            assertTrue(it.isFailure)
            assertEquals(msgForInvalidUser,it.exceptionOrNull()?.message)
        }
    }

    @Test
    fun `login with incorrect password`() : Unit = runBlocking {
        val results = usersRepo.login(fakeUserWithInvalidPassword)
        results.let{
            assertTrue(it.isFailure)
            assertEquals(msgForInvalidPassword,it.exceptionOrNull()?.message)
        }
    }

}