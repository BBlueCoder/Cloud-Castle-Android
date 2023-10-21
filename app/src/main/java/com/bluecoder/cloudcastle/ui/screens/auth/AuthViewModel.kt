package com.bluecoder.cloudcastle.ui.screens.auth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluecoder.cloudcastle.R
import com.bluecoder.cloudcastle.data.pojoclasses.User
import com.bluecoder.cloudcastle.data.pojoclasses.UserJWT
import com.bluecoder.cloudcastle.data.repos.UsersRepo
import com.bluecoder.cloudcastle.utils.SharedPreferencesManager
import com.bluecoder.cloudcastle.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val usersRepo: UsersRepo,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val _userAuthenticatingState = MutableStateFlow<Result<UserJWT>?>(null)
    val userAuthenticatingUIState: StateFlow<Result<UserJWT>?>
        get() = _userAuthenticatingState.asStateFlow()

    private var _currentUser = mutableStateOf(User("", ""))
    val currentUser: State<User>
        get() = _currentUser


    val usernameErrorState: MutableState<Utils.UIText> =
        mutableStateOf(Utils.UIText.DynamicString(""))
    val passwordErrorState: MutableState<Utils.UIText> =
        mutableStateOf(Utils.UIText.DynamicString(""))

    fun updateUser(username: String?, password: String?) {
        _currentUser.value =
            User(username ?: currentUser.value.username, password ?: currentUser.value.password)
        username?.let {
            usernameErrorState.value = Utils.UIText.DynamicString("")
        }
        password?.let {
            passwordErrorState.value = Utils.UIText.DynamicString("")
        }
    }

    fun signup() {
        if (!isInputsValid())
            return

        viewModelScope.launch(Dispatchers.IO) {
            val result = usersRepo.signup(currentUser.value)
            _userAuthenticatingState.emit(result)
            if (result.isSuccess)
                saveUserCredentials(currentUser.value, result.getOrNull()!!.token)
        }
    }

    fun login() {

        if (!isInputsValid())
            return

        viewModelScope.launch(Dispatchers.IO) {
            val result = usersRepo.login(currentUser.value)
            _userAuthenticatingState.emit(result)
            if (result.isSuccess)
                saveUserCredentials(currentUser.value, result.getOrNull()!!.token)
        }
    }

    private fun saveUserCredentials(user: User, token: String) {
        sharedPreferencesManager.addPreference(SharedPreferencesManager.USERNAME_KEY, user.username)
        sharedPreferencesManager.addPreference(SharedPreferencesManager.PASSWORD_KEY, user.password)
        sharedPreferencesManager.addPreference(SharedPreferencesManager.TOKEN_KEY, token)
    }

    fun clearUserData() {
        _currentUser.value = User("", "")
        usernameErrorState.value = Utils.UIText.DynamicString("")
        passwordErrorState.value = Utils.UIText.DynamicString("")

        viewModelScope.launch(Dispatchers.IO) {
            _userAuthenticatingState.emit(null)
        }
    }

    private fun isInputsValid(): Boolean {
        isUsernameValid()
        isPasswordValid()

        if (usernameErrorState.value is Utils.UIText.StringResource || passwordErrorState.value is Utils.UIText.StringResource)
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