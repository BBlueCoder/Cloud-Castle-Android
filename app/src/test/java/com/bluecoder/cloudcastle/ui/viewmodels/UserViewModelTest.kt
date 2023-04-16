package com.bluecoder.cloudcastle.ui.viewmodels

import com.bluecoder.cloudcastle.core.data.User
import com.bluecoder.cloudcastle.core.repos.UsersRepoMock
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserViewModelTest{
    private lateinit var viewModel: UserViewModel

    @Before
    fun setup(){
        viewModel = UserViewModel(UsersRepoMock())
    }

    @Test
    fun `test duplicate username`() = run {

        viewModel.signup(User("user","123"))
    }
}