package com.example.scoot_scoot.android.ViewModels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoot_scoot.android.Data.UserData
import com.example.scoot_scoot.android.Data.UserManager
import com.example.scoot_scoot.android.Repository.UserRepository
import kotlinx.coroutines.launch

class BalanceViewModel: ViewModel(){
    val userRepository = UserRepository()
    val userId = UserManager.getUserId()
    var user =   mutableStateOf(UserData())
    var userBalance =  mutableIntStateOf(0)

    init {
        viewModelScope.launch {
            user = mutableStateOf(userRepository.getUserById(userId)!!)
            userBalance.intValue = user.value.credit
        }
    }


    fun updateUserBalance(){
        viewModelScope.launch {
            userRepository.updateUser(
                userId,
                user.value
            )
            user.value=userRepository.getUserById(userId)!!
            userBalance.value=user.value.credit
        }
    }

}