package com.example.scoot_scoot.android.Network

object ScooterClient:NetworkClient() {

    fun getScooterById(id: Int): String? {
        val url = "${baseUrl}scooter/getScooter/$id"
        return getRequest(url)
    }

}