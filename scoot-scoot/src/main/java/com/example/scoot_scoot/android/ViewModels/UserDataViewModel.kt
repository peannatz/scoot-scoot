package com.example.scoot_scoot.android.ViewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoot_scoot.android.Data.UserData
import com.example.scoot_scoot.android.Repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class UserDataViewModel(private val userId: Int? = null) : ViewModel() {

    //var userData: UserData = UserData()
    protected val userRepository = UserRepository()

    private val _fetchedUserData = MutableLiveData<UserData>()
    var userData = UserData()

    init {
        if (userId != null) {
            getData()
        }
    }

    private fun getData() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                try {
                    userData = userRepository.getUserById(userId!!)!!

                } catch (e: Exception) {
                    TODO("Error Handling")
                }
            }
        }
    }

    fun updateUser(updatedUser: UserData) {

        //TODO: bessere Lösung finden das ist glaub ich etwas unnötiges hin und her
        _fetchedUserData.postValue(userData)
        userData = updatedUser
        name.value = userData.name
        surname.value = userData.surname
        email.value = userData.email
        password.value = userData.password
        birthdate.value = userData.birthdate
    }

    var name: MutableState<String> = mutableStateOf(userData.name)
    var isNameInvalid: MutableState<Boolean> = mutableStateOf(false)
    var nameErrMsg: MutableState<String> = mutableStateOf("")

    var surname: MutableState<String> = mutableStateOf(userData.surname)
    var isSurnameInvalid: MutableState<Boolean> = mutableStateOf(false)
    var surnameErrMsg: MutableState<String> = mutableStateOf("")

    var email: MutableState<String> = mutableStateOf(userData.email)
    var isEmailInvalid: MutableState<Boolean> = mutableStateOf(false)
    var emailErrMsg: MutableState<String> = mutableStateOf("")

    var password: MutableState<String> = mutableStateOf(userData.password)
    var isPasswordInvalid: MutableState<Boolean> = mutableStateOf(false)
    var passwordErrMsg: MutableState<String> = mutableStateOf("")

    var confirmPassword: MutableState<String> = mutableStateOf("")
    var isConfirmPasswordInvalid: MutableState<Boolean> = mutableStateOf(false)
    var confPasswordErrMsg: MutableState<String> = mutableStateOf("")

    var birthdate: MutableState<String> = mutableStateOf(userData.birthdate)

    abstract fun handleInputChange()

    fun validateName() {
        if (name.value.trim().isEmpty()) {
            isNameInvalid.value = true
            nameErrMsg.value = "Name can't be empty"
        } else {
            isNameInvalid.value = false
            nameErrMsg.value = ""
        }
        handleInputChange()
    }

    fun validateSurname() {
        if (surname.value.trim().isEmpty()) {
            isSurnameInvalid.value = true
            surnameErrMsg.value = "Surname can't be empty"
        } else {
            isSurnameInvalid.value = false
            surnameErrMsg.value = ""
        }
        handleInputChange()
    }

    fun validateEmail() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            isEmailInvalid.value = true
            emailErrMsg.value = "Input proper email id"
        } else {
            isEmailInvalid.value = false
            emailErrMsg.value = ""
        }
        handleInputChange()
    }

    fun validatePassword() {
        if (password.value.length < 8) {
            isPasswordInvalid.value = true
            passwordErrMsg.value = "Min. password length: 8"
        } else {
            isPasswordInvalid.value = false
            passwordErrMsg.value = ""
        }
        handleInputChange()
    }

    fun validateConfirmPassword() {
        if (password.value != confirmPassword.value) {
            isConfirmPasswordInvalid.value = true
            confPasswordErrMsg.value = "Password did not match"
        } else {
            isConfirmPasswordInvalid.value = false
            confPasswordErrMsg.value = ""
        }
        handleInputChange()
    }

}