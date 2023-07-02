package com.example.scoot_scoot.android.Data


data class Scooter(
    val id: Long,
    val name: String,
    val battery: Int,
    val available: Boolean,
    val location: Location,
)
