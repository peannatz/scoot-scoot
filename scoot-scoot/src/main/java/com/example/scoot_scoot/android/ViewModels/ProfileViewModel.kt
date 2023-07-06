package com.example.scoot_scoot.android.ViewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.scoot_scoot.android.Data.UserDataModel
import com.example.scoot_scoot.android.Data.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : UserDataViewModel() {

    lateinit var nameData: UserDataModel;
    lateinit var surnameData: UserDataModel;
    lateinit var emailData: UserDataModel;
    lateinit var birthdateData: UserDataModel;

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val userId=UserManager.getUserId()
            val fetchedUser = userRepository.getUserById(userId)!!
            updateUser(fetchedUser)
            userFetched.value = true
            nameData = UserDataModel("Name", name, nameEdited) { validateName() }
            surnameData = UserDataModel("Surname", surname, surnameEdited) { validateSurname() }
            emailData = UserDataModel("Email", email, emailEdited) { validateEmail() }
            birthdateData = UserDataModel("Birthdate", birthdate, mutableStateOf(false)) { }
        }
    }

    val nameEdited: MutableState<Boolean> = mutableStateOf(false)

    val userFetched: MutableState<Boolean> = mutableStateOf(false)

    val surnameEdited: MutableState<Boolean> = mutableStateOf(false)

    val emailEdited: MutableState<Boolean> = mutableStateOf(false)


    override fun handleInputChange() {

        if (nameData.edited.value) {
            userData.name = nameData.data.value
        }
        if (surnameData.edited.value) {
            userData.surname = surnameData.data.value
        }
        if (emailData.edited.value) {
            userData.email = emailData.data.value
        }

        viewModelScope.launch(Dispatchers.IO) {
            val userId=UserManager.getUserId()
            userRepository.updateUser(userId, userData)
        }
    }

}