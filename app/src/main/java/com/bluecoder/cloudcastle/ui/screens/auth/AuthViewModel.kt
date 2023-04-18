package com.bluecoder.cloudcastle.ui.screens.auth

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluecoder.cloudcastle.core.data.User
import com.bluecoder.cloudcastle.core.data.UserJWT
import com.bluecoder.cloudcastle.core.repos.users.UsersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val usersRepo: UsersRepo) : ViewModel() {

    private val _userAuthenticatingState = MutableStateFlow<Result<UserJWT>?>(null)
    val userAuthenticatingState = _userAuthenticatingState.asStateFlow()
    var currentUser by mutableStateOf(User("",""))
        private set

    fun updateUser(username : String = currentUser.username,password : String = currentUser.password){
        currentUser = User(username,password)
    }

    fun signup(){
        viewModelScope.launch(Dispatchers.IO){
            usersRepo.signup(currentUser).collect{
                _userAuthenticatingState.emit(it)
            }
        }
    }

    fun login(){
        viewModelScope.launch(Dispatchers.IO){
            usersRepo.login(currentUser).collect{
                _userAuthenticatingState.emit(it)
            }
        }
    }

    fun clearUserData(){
        currentUser = User("","")
        viewModelScope.launch(Dispatchers.IO){
            _userAuthenticatingState.emit(null)
        }
    }
}