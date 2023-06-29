package com.example.scoot_scoot.android.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scoot_scoot.android.DAO.ScooterDao
import com.example.scoot_scoot.android.Entity.Scooter

@Database(
    entities = [Scooter::class],
    version = 1
)
abstract class ScooterDatabase : RoomDatabase(){

    abstract val dao: ScooterDao
}