package com.example.scoot_scoot.android.Repository

import com.example.scoot_scoot.android.Data.UserData
import com.example.scoot_scoot.android.Network.UserClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {

    suspend fun checkLogin(email: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = UserClient.getUserByEmail(email, password)
            user?.password == password
        }
    }

    suspend fun registerUser(user: UserData): UserData {
        return withContext(Dispatchers.IO) {
            UserClient.addUser(user)
        }
    }

    suspend fun checkForRunningRides(id: Int): Boolean {
        val user: UserData
        withContext(Dispatchers.IO) {
            user = UserClient.getUserByID(id)!!
        }
        if (user.rides.size > 0 && user.rides.last().rideLength == null) {
            return true
        }
        return false
    }

    suspend fun getUserById(id: Int): UserData? {
        return withContext(Dispatchers.IO) {
            UserClient.getUserByID(id)
        }
    }

    suspend fun getUserByEmail(email: String, password: String): UserData {
        return withContext(Dispatchers.IO) {
            UserClient.getUserByEmail(email, password) as UserData
        }
    }

    suspend fun updateUser(id: Int, user: UserData): Boolean {
        return withContext(Dispatchers.IO) {
            UserClient.updateUser(id, user)
        }
    }
}