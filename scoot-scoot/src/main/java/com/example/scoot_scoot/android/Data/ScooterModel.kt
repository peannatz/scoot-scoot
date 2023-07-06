package com.example.scoot_scoot.android.Data

import com.google.gson.annotations.Expose


data class ScooterModel(
    val id: Int,
    val name: String,
    val battery: Int,
    val available: Boolean,
    var location: Location,

    @Expose(serialize = false)
    val tierType: Tier,
)
