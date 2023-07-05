package com.example.scoot_scoot.android.Data


data class ScooterModel(
    val id: Long,
    val name: String,
    val battery: Int,
    val available: Boolean,
    val location: Location,
    val tierType: Tier,
)
