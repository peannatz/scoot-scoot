package com.example.scoot_scoot.android.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert
import com.example.scoot_scoot.android.Entity.Scooter

@Dao
interface ScooterDao {

    @Upsert
    fun insertScooter(scooter: Scooter)

    @Delete
    fun deleteScooter(scooter: Scooter )
}