package com.example.scoot_scoot.android.Network

import com.example.scoot_scoot.android.Data.UserData
import com.google.gson.Gson

object UserClient : NetworkClient() {

    val gson = Gson()

    //Add User

    fun addUser(userData: UserData): Boolean {

        val url = "${baseUrl}user/add"
        val jsonPayload = gson.toJson(userData)
        return postRequest(url, jsonPayload)
    }

    //Get User

    fun getUserByID(id: Int): UserData {

        val url = "${baseUrl}user/getbyId/$id"
        val userJson = getRequest(url)
        return gson.fromJson(userJson, UserData::class.java)
    }

    fun updateUser(id: Int, userData: UserData): Boolean {
        val url = "${baseUrl}update/$id"
        val jsonPayload = gson.toJson(userData)
        return postRequest(url, jsonPayload)
    }
}