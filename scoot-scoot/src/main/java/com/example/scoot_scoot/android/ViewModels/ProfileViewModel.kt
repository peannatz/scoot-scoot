package com.example.scoot_scoot.android.ViewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.scoot_scoot.android.Network.UserClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : UserDataViewModel() {


    //TODO:exchange hard coded id for account id
    init {
        CoroutineScope(Dispatchers.IO).launch {
            val fetchedUser = UserClient.getUserByID(3)
            updateUser(fetchedUser)
            userFetched.value = true
        }
    }

    val nameEdited: MutableState<Boolean> = mutableStateOf(false)

    val userFetched: MutableState<Boolean> = mutableStateOf(false)

    val surnameEdited: MutableState<Boolean> = mutableStateOf(false)

    val emailEdited: MutableState<Boolean> = mutableStateOf(false)

    override fun handleInputChange() {
        //UserClient.UpdateUser(2, userData)
    }

}