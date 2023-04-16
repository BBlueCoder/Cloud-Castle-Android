package com.bluecoder.cloudcastle.core.repos

import com.bluecoder.cloudcastle.core.data.User
import com.bluecoder.cloudcastle.core.data.UserJWT
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UsersRepoMockTest{
    private lateinit var repo : UsersRepoMock

    private val validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODE0MTgzMjEsImRhdGEiOnsiaWQiOjMsInVzZXJuYW1lIjoidXNlcjIifSwiaWF0IjoxNjgxNDE2MjIxfQ.FrF6qw730u1SMAIWy0PsY4YzxJG-a-UWPBpwQuvO0_U"

    @Before
    fun setup(){
        repo = UsersRepoMock()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test duplicate username when signup`() = runTest {

        launch(UnconfinedTestDispatcher()){
            repo.signup(User("user","123")).collectLatest{
                assertTrue(it.isFailure)
                assertTrue(it.exceptionOrNull() is Exception)
                assertEquals("Username is duplicated, please change your username", it.exceptionOrNull()?.message)
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test successful signup`() = runTest {
         repo.signup(User("user1","123")).collectLatest {
             assertTrue(it.isSuccess)
             assertTrue(it.getOrNull() is UserJWT)
             assertEquals(validToken,it.getOrNull()?.token)
         }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test login with invalid username`() = runTest {
        repo.login(User("user2","123")).collectLatest {
            assertTrue(it.isFailure)
            assertTrue(it.exceptionOrNull() is Exception)
            assertEquals("User not found, please enter a valid username",it.exceptionOrNull()?.message)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test login with incorrect password`() = runTest{
        repo.login(User("user","1234")).collectLatest {
            assertTrue(it.isFailure)
            assertTrue(it.exceptionOrNull() is Exception)
            assertEquals("Password is incorrect",it.exceptionOrNull()?.message)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `test login with valid user`() = runTest {
        repo.login(User("user","123")).collectLatest {
            assertTrue(it.isSuccess)
            assertTrue(it.getOrNull() is UserJWT)
            assertEquals(validToken,it.getOrNull()?.token)
        }
    }
}