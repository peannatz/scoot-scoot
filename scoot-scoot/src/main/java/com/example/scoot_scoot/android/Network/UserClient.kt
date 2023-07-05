package com.example.scoot_scoot.android.Network

import com.example.scoot_scoot.android.Data.UserData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat
import java.util.Locale

object UserClient : NetworkClient() {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ENGLISH)

    val gson = GsonBuilder().setDateFormat(dateFormat.toPattern()).create()

    //Add User

    fun addUser(userData: UserData): UserData {

        val url = "${baseUrl}user/add"
        val jsonPayload = gson.toJson(userData)

        val response=postRequest(url, jsonPayload)
        if(response!=null){
            return gson.fromJson(response, UserData::class.java)
        }
        return UserData()
    }

    //Get User

    fun getUserByID(id: Int): UserData {

        val url = "${baseUrl}user/getbyId/$id"
        val userJson = getRequest(url)
        return gson.fromJson(userJson, UserData::class.java)
    }

    fun getUserByEmail(email: String, password: String): UserData? {

        val url = "${baseUrl}user/getByEmail/${email}?password=${password}"
        val userJson = getRequest(url) ?: return null
        return gson.fromJson(userJson, UserData::class.java)
    }

    //Update User

    //TODO: response evtl ändern und den user zurück werfen?

    fun updateUser(id: Int, userData: UserData): Boolean {
        val url = "${baseUrl}user/update/$id"
        val jsonPayload = gson.toJson(userData)
        val response=postRequest(url, jsonPayload)

        if(response!=null){
            return true
        }
        return false
    }
}