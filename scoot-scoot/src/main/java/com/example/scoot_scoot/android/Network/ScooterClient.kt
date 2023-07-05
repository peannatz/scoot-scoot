package com.example.scoot_scoot.android.Network

import com.example.scoot_scoot.android.Data.ScooterModel
import com.example.scoot_scoot.android.Data.UserData
import com.example.scoot_scoot.android.Network.UserClient.gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object ScooterClient:NetworkClient() {

    private val scooterListType: Type = object : TypeToken<List<ScooterModel>>() {}.type

    fun getScooterById(id: Int): ScooterModel? {
        val url = "${baseUrl}scooter/getScooter/$id"
        val scooterJson = getRequest(url) ?: return null
        return gson.fromJson(scooterJson, ScooterModel::class.java)
    }

    fun getAllScooters(): List<ScooterModel> {
        val url = "${baseUrl}scooter/getAllScooters"
        val scooterJson = getRequest(url) ?: return listOf()
        return gson.fromJson(scooterJson, scooterListType)
    }
    fun getScootersByAvailability(available: Boolean): List<ScooterModel> {
        val url = "${baseUrl}scooter/getAllAvailableScooters/$available"
        val scooterJson = getRequest(url) ?: return listOf()
        return listOf(gson.fromJson(scooterJson, scooterListType))
    }
    fun getScootersByBattery(battery: Int): List<ScooterModel> {
        val url = "${baseUrl}scooter/getByBattery/$battery"
        val scooterJson = getRequest(url) ?: return listOf()
        return listOf(gson.fromJson(scooterJson, scooterListType))
    }

    fun updateScooterbyId(id: Int, scooter:ScooterModel):Boolean {

        val url = "${baseUrl}scooter/update/$id"
        val jsonPayload = gson.toJson(scooter)

        val response= postRequest(url, jsonPayload)
        if(response!=null){
            return true
        }
        return false
    }



}