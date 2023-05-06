package com.bluecoder.cloudcastle.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.bluecoder.cloudcastle.core.data.User
import com.bluecoder.cloudcastle.core.data.UserJWT
import com.bluecoder.cloudcastle.utils.Utils
import kotlinx.coroutines.flow.StateFlow

interface AuthViewModelInterface {

    val userAuthenticatingUIState : StateFlow<Result<UserJWT>?>
    val currentUser : State<User>

    val usernameErrorState : MutableState<Utils.UIText>
    val passwordErrorState : MutableState<Utils.UIText>

    fun updateUser(username : String?, password : String?)
    fun signup()
    fun login()
    fun clearUserData()


}