package com.example.scoot_scoot.android.Service

import com.example.scoot_scoot.android.Components.MarkerInfoBottomSheet
import com.example.scoot_scoot.android.ViewModels.RideViewModel
import java.time.Duration
import java.time.LocalDateTime

object PriceService {

    fun calculatePriceTime(startTime: LocalDateTime, endTime: LocalDateTime): Long {
        val baseRate = 10
        val duration = Duration.between(startTime, endTime)
        val durationInMinutes = duration.toMinutes()

        return durationInMinutes * baseRate
    }

    //TODO: pricekm ändern

    fun convertToCurrencyStringKm(distance: Int, price: Int): String {
        val euros = distance / 1000 * price / 100.0
        val formattedString = String.format("%.2f", euros)
        return "$formattedString€"
    }

    fun convertToCurrency(amountInCent: Int): String {
        val euros = amountInCent / 100.0
        val formattedString = String.format("%.2f", euros)
        return "$formattedString€"
    }
}