package com.example.scoot_scoot.android.ViewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoot_scoot.android.Data.UserData
import com.example.scoot_scoot.android.Repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var loginCallback: LoginCallback? = null

    var email: MutableState<String> = mutableStateOf("")
    var password: MutableState<String> = mutableStateOf("")

    private val userRepository = UserRepository()
    val loginErrorMsg = mutableStateOf("")

    suspend fun checkLoginData(loginCallback: LoginCallback) {
        viewModelScope.launch {
            val loggedIn = userRepository.checkLogin(email.value, password.value)
            if (loggedIn) {
                loginErrorMsg.value = ""
                val user=userRepository.getUserByEmail(email.value)
                loginCallback.onLoginSuccess(user)
            } else {
                userNotFound()
            }
        }
    }

    private fun userNotFound() {
        loginErrorMsg.value = "Wrong Email or Password"
        loginCallback?.onLoginError("Wrong Email or Password")
    }

}


interface LoginCallback {
    fun onLoginSuccess(user: UserData)
    fun onLoginError(errorMessage: String)
}