package com.example.scoot_scoot.android.Data

import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

data class RideModel(
    var id: Int,
    var startTime: Date,
    var rideLength: Int,
    var price: Int,
    var startLocation: Location,
    var endLocation: Location
)
