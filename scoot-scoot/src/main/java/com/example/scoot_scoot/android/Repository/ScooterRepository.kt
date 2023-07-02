package com.example.scoot_scoot.android.Repository

import com.example.scoot_scoot.android.Data.Scooter
import com.example.scoot_scoot.android.Data.UserData
import com.example.scoot_scoot.android.Network.ScooterClient
import com.example.scoot_scoot.android.Network.UserClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScooterRepository {

    suspend fun getScooterById(id: Int): Scooter? {
        return withContext(Dispatchers.IO) {
            ScooterClient.getScooterById(id)
        }
    }

    suspend fun getAllScooters(): List<Scooter> {
        return withContext(Dispatchers.IO) {
            ScooterClient.getAllScooters()
        }
    }

    suspend fun getScooterByAvailability(available: Boolean): List<Scooter> {
        return withContext(Dispatchers.IO) {
            ScooterClient.getScootersByAvailability(available)
        }
    }

    suspend fun getScootersByBattery(battery: Int): List<Scooter> {
        return withContext(Dispatchers.IO) {
            ScooterClient.getScootersByBattery(battery)
        }
    }
}