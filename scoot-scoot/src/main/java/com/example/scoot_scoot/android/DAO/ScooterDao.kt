package com.example.scoot_scoot.android.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.scoot_scoot.android.Entity.Scooter

@Dao
interface ScooterDao {

    @Upsert
    fun insertScooter(scooter: Scooter)

    @Delete
    fun deleteScooter(scooter: Scooter )

    @Query("SELECT * FROM Scooter WHERE name LIKE :name")
    fun findByName(name: String): Scooter

    @Query("SELECT id FROM Scooter WHERE x LIKE :x AND y LIKE :y")
    fun findByPosition(x: Int, y: Int): Scooter
    @Query("SELECT * FROM Scooter")
    fun getAll(): List<Scooter>

    @Query("SELECT priceMin FROM Scooter WHERE id LIKE :id")
    fun getPriceByMin(id:Int): Int
    @Query("SELECT priceKm FROM Scooter WHERE id LIKE :id")
    fun getPriceByKM(id: Int): Int
}