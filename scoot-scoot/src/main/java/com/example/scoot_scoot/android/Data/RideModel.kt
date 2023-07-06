package com.example.scoot_scoot.android.Data

import java.time.Instant
import java.time.LocalDateTime
import java.util.Date

data class RideModel(
    var id: Int? = null,
    var startTime: Date,
    var rideLength: Int? = null,
    var price: Int? = null,
    var startLocation: Location,
    var endLocation: Location? = null
)
