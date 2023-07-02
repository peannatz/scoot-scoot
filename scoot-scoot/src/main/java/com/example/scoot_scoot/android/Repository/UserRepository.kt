package com.example.scoot_scoot.android.Repository

import com.example.scoot_scoot.android.Data.UserData
import com.example.scoot_scoot.android.Network.UserClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {

    suspend fun checkLogin(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = UserClient.getUserByEmail(email)
            user?.password == password
        }
    }

    suspend fun registerUser(user: UserData){

    }
}