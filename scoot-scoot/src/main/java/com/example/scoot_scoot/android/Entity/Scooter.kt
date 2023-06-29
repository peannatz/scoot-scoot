package com.example.scoot_scoot.android.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Scooter(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val x: Int,
    val y: Int,
    val battery: Int,
    val available: Boolean,
    val description: String,
    val priceMin: Int,
    val priceKm: Int,

)
