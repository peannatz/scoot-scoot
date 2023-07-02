package com.example.scoot_scoot.android.Network

import com.example.scoot_scoot.android.Data.ScooterModel
import com.example.scoot_scoot.android.Network.UserClient.gson

object ScooterClient:NetworkClient() {

    fun getScooterById(id: Int): ScooterModel? {
        val url = "${baseUrl}scooter/getScooter/$id"
        val scooterJson = getRequest(url) ?: return null
        return gson.fromJson(scooterJson, ScooterModel::class.java)
    }

    fun getAllScooters(): List<ScooterModel> {
        val url = "${baseUrl}scooter/getAllScooters"
        val scooterJson = getRequest(url) ?: return listOf()
        return listOf(gson.fromJson(scooterJson, ScooterModel::class.java))
    }
    fun getScootersByAvailability(available: Boolean): List<ScooterModel> {
        val url = "${baseUrl}scooter/getAllAvailableScooters/$available"
        val scooterJson = getRequest(url) ?: return listOf()
        return listOf(gson.fromJson(scooterJson, ScooterModel::class.java))
    }
    fun getScootersByBattery(battery: Int): List<ScooterModel> {
        val url = "${baseUrl}scooter/getByBattery/$battery"
        val scooterJson = getRequest(url) ?: return listOf()
        return listOf(gson.fromJson(scooterJson, ScooterModel::class.java))
    }


}