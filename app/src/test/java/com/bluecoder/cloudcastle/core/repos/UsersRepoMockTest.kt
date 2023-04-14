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

    @Before
    fun setup(){
        repo = UsersRepoMock()
    }

    @Test
    fun `test duplicate username`() = runTest {

        var result : Result<UserJWT>? = null

        launch(UnconfinedTestDispatcher()){
            repo.signup(User("user","123")).collectLatest{
                result = it
            }
        }

        assertTrue(result!!.isFailure)
        assertTrue(result!!.exceptionOrNull() is Exception)
        assertEquals("Username is duplicated, please change your username", result!!.exceptionOrNull()?.message)

    }

}