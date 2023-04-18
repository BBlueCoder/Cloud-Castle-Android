package com.bluecoder.cloudcastle.core.repos.users

import com.bluecoder.cloudcastle.core.api.ServerAPI
import com.bluecoder.cloudcastle.core.data.FileItem
import com.bluecoder.cloudcastle.core.data.User
import com.bluecoder.cloudcastle.core.data.UserJWT
import com.bluecoder.cloudcastle.core.repos.files.DefaultFilesRepo
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class DefaultUsersRepoTest {

    @Mock
    private lateinit var serverAPI: ServerAPI

    private lateinit var defaultUsersRepo: DefaultUsersRepo

    private val validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODE0MTgzMjEsImRhdGEiOnsiaWQiOjMsInVzZXJuYW1lIjoidXNlcjIifSwiaWF0IjoxNjgxNDE2MjIxfQ.FrF6qw730u1SMAIWy0PsY4YzxJG-a-UWPBpwQuvO0_U"

    @Before
    fun setup(){
        MockitoAnnotations.openMocks(this)
        defaultUsersRepo = DefaultUsersRepo(serverAPI)
    }

    @Test
    fun `signup with valid user`(): Unit = runBlocking {
        val response = Response.success(UserJWT(validToken))
        val user = User("user","123")

        `when`(serverAPI.signup(user)).thenReturn(response)

        val results = defaultUsersRepo.signup(user)
        results.collect{
            assertTrue(it.isSuccess)
            assertEquals(validToken,it.getOrNull()?.token)
        }
    }

    @Test
    fun `signup with duplicate user`() : Unit = runBlocking {
        val msg = "Username is duplicated, please change your username"
        val errorBody = ResponseBody.create(MediaType.parse("application/json"),msg)
        val response = Response.error<UserJWT>(401,errorBody)

        val user = User("user","123")

        `when`(serverAPI.signup(user)).thenReturn(response)


        val results = defaultUsersRepo.signup(user)
        results.collect{
            assertTrue(it.isFailure)
            assertEquals(msg,it.exceptionOrNull()?.message)
        }
    }

    @Test
    fun `login with valid user`() : Unit = runBlocking {
        val response = Response.success(UserJWT(validToken))
        val user = User("user","123")

        `when`(serverAPI.login(user)).thenReturn(response)

        val results = defaultUsersRepo.login(user)
        results.collect{
            assertTrue(it.isSuccess)
            assertEquals(validToken,it.getOrNull()?.token)
        }
    }

    @Test
    fun `login with invalid username`() : Unit = runBlocking {
        val msg = "User not found, please enter a valid username"
        val errorBody = ResponseBody.create(MediaType.parse("application/json"),msg)
        val response = Response.error<UserJWT>(401,errorBody)

        val user = User("user","123")

        `when`(serverAPI.login(user)).thenReturn(response)

        val results = defaultUsersRepo.login(user)
        results.collect{
            assertTrue(it.isFailure)
            assertEquals(msg,it.exceptionOrNull()?.message)
        }
    }

    @Test
    fun `login with incorrect password`() : Unit = runBlocking {
        val msg = "Password is incorrect"
        val errorBody = ResponseBody.create(MediaType.parse("application/json"),msg)
        val response = Response.error<UserJWT>(401,errorBody)

        val user = User("user","123")

        `when`(serverAPI.login(user)).thenReturn(response)

        val results = defaultUsersRepo.login(user)
        results.collect{
            assertTrue(it.isFailure)
            assertEquals(msg,it.exceptionOrNull()?.message)
        }
    }

}