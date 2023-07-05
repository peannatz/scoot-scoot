package com.example.scoot_scoot.android.Data

import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

data class RideModel(
    var id: Int? = null,
    var startTime: Date,
    var rideLength: Int = 0,
    var price: Int = 0,
    var startLocation: Location,
    var endLocation: Location? = null
)
