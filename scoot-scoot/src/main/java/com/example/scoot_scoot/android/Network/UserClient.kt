package com.example.scoot_scoot.android.Network

import com.example.scoot_scoot.android.Data.UserData
import com.google.gson.Gson

object UserClient : NetworkClient() {

    val gson = Gson()

    //Add User

    fun addUser(userData: UserData): UserData {

        val url = "${baseUrl}user/add"
        val jsonPayload = gson.toJson(userData)
        //return gson.fromJson(userJson, UserData::class.java)
        if(postRequest(url, jsonPayload)){
            return getUserByID(2)
        }
        return UserData()
    }

    //Get User

    fun getUserByID(id: Int): UserData {

        val url = "${baseUrl}user/getbyId/$id"
        val userJson = getRequest(url)
        return gson.fromJson(userJson, UserData::class.java)
    }

    fun getUserByEmail(email: String): UserData? {

        val url = "${baseUrl}user/getByEmail/$email"
        val userJson = getRequest(url) ?: return null
        return gson.fromJson(userJson, UserData::class.java)
    }

    //Update User

    fun updateUser(id: Int, userData: UserData): Boolean {
        val url = "${baseUrl}user/update/$id"
        val jsonPayload = gson.toJson(userData)
        return postRequest(url, jsonPayload)
    }
}