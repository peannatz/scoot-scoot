package com.example.scoot_scoot.android.Repository

import com.example.scoot_scoot.android.Data.ScooterModel
import com.example.scoot_scoot.android.Network.ScooterClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScooterRepository {

    suspend fun getScooterById(id: Int): ScooterModel? {
        return withContext(Dispatchers.IO) {
            ScooterClient.getScooterById(id)
        }
    }

    suspend fun getAllScooters(): List<ScooterModel> {
        return withContext(Dispatchers.IO) {
            ScooterClient.getAllScooters()
        }
    }

    suspend fun getScooterByAvailability(available: Boolean): List<ScooterModel> {
        return withContext(Dispatchers.IO) {
            ScooterClient.getScootersByAvailability(available)
        }
    }

    suspend fun getScootersByBattery(battery: Int): List<ScooterModel> {
        return withContext(Dispatchers.IO) {
            ScooterClient.getScootersByBattery(battery)
        }
    }
}