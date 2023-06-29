package com.example.scoot_scoot.android.Service

import java.time.Duration
import java.time.LocalDateTime

class PriceService {

    fun calculatePriceTime(startTime : LocalDateTime, endTime : LocalDateTime): Long {
        val baseRate = 10
        val duration = Duration.between(startTime, endTime)
        val durationInMinutes = duration.toMinutes()

        return durationInMinutes * baseRate
    }
}