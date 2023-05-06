package com.bluecoder.cloudcastle.ui.screens.viewmodels

import androidx.activity.ComponentActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.core.data.User
import com.bluecoder.cloudcastle.core.data.UserJWT
import com.bluecoder.cloudcastle.ui.viewmodels.AuthViewModelInterface
import com.bluecoder.cloudcastle.utils.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModelMock(private val activity: ComponentActivity) : AuthViewModelInterface {

    private var fakeUser = mutableStateOf(User("user","123"))
    private var fakeResult = MutableStateFlow<Result<UserJWT>?>(null)
    private val validToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODE0MTgzMjEsImRhdGEiOnsiaWQiOjMsInVzZXJuYW1lIjoidXNlcjIifSwiaWF0IjoxNjgxNDE2MjIxfQ.FrF6qw730u1SMAIWy0PsY4YzxJG-a-UWPBpwQuvO0_U"


    override val userAuthenticatingUIState: StateFlow<Result<UserJWT>?>
        get() = fakeResult.asStateFlow()

    private var _currentUser = mutableStateOf(User("",""))
    override val currentUser: State<User>
        get() = _currentUser

    override val usernameErrorState: MutableState<Utils.UIText> =
        mutableStateOf(Utils.UIText.DynamicString(""))
    override val passwordErrorState: MutableState<Utils.UIText> =
        mutableStateOf(Utils.UIText.DynamicString(""))

    override fun updateUser(username: String?, password: String?) {
        _currentUser.value = User(username?: _currentUser.value.username, password?: _currentUser.value.password)
        username?.let {
            usernameErrorState.value = Utils.UIText.DynamicString("")
        }
        password?.let {
            passwordErrorState.value = Utils.UIText.DynamicString("")
        }
    }

    override fun signup() {
        if(!isInputsValid())
            return

        if(currentUser.value.username == fakeUser.value.username){
            fakeResult.value = Result.failure(Exception(activity.getString(R.string.duplicate_username)))
            return
        }
        fakeResult.value = Result.success(UserJWT(validToken))
    }

    override fun login() {
        if(!isInputsValid())
            return

        if(currentUser.value.username != fakeUser.value.username){
            fakeResult.value = Result.failure(Exception(activity.getString(R.string.user_not_found)))
            return
        }

        if(currentUser.value.password != fakeUser.value.password){
            fakeResult.value = Result.failure(Exception(activity.getString(R.string.invalid_password)))
            return
        }

        fakeResult.value = Result.success(UserJWT(validToken))
    }

    override fun clearUserData() {
        _currentUser.value = User("","")
        usernameErrorState.value = Utils.UIText.DynamicString("")
        passwordErrorState.value = Utils.UIText.DynamicString("")
    }

    private fun isInputsValid() : Boolean{
        isUsernameValid()
        isPasswordValid()

        if(usernameErrorState.value is Utils.UIText.StringResource || passwordErrorState.value is Utils.UIText.StringResource)
            return false
        return true
    }
    private fun isUsernameValid() {
        if (currentUser.value.username == "") {
            usernameErrorState.value = Utils.UIText.StringResource(R.string.empty_username_field)
        }
    }

    private fun isPasswordValid() {
        if (currentUser.value.password == "") {
            passwordErrorState.value = Utils.UIText.StringResource(R.string.empty_password_field)
        }
    }
}