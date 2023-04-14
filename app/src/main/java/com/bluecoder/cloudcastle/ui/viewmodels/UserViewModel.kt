package com.bluecoder.cloudcastle.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluecoder.cloudcastle.core.data.User
import com.bluecoder.cloudcastle.core.data.UserJWT
import com.bluecoder.cloudcastle.core.repos.users.DefaultUsersRepo
import com.bluecoder.cloudcastle.core.repos.users.UsersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val usersRepo: UsersRepo) : ViewModel() {

    private val _currentUser = MutableStateFlow<Result<UserJWT>?>(null)
    val currentUser = _currentUser.asStateFlow()

    fun signup(user : User){
        viewModelScope.launch(Dispatchers.IO){
            usersRepo.signup(user).collect{
                _currentUser.emit(it)
            }
        }
    }

    fun login(user : User){
        viewModelScope.launch(Dispatchers.IO){
            usersRepo.login(user).collect{
                _currentUser.emit(it)
            }
        }
    }
}