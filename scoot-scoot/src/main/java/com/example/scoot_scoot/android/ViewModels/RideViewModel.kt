package com.example.scoot_scoot.android.ViewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.scoot_scoot.android.Data.AutoCompleteResult
import com.example.scoot_scoot.android.Data.RouteModel
import com.example.scoot_scoot.android.Data.UserManager
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.concurrent.TimeUnit

class RideViewModel() : ViewModel() {
    var riding by mutableStateOf(false)
    var kmMode by mutableStateOf(false)
    var passedTimeString by mutableStateOf("")
    var passedTimeInMinutes by mutableIntStateOf(0)
    lateinit var startTime: MutableState<Date>
    var destination = mutableStateOf(AutoCompleteResult("", ""))
    var route by mutableStateOf(RouteModel(0,""))

    fun getStartTime() {
        startTime = mutableStateOf(UserManager.getStartTime())
    }


    fun getFormattedTimeString(): String {
        var formatter = DateTimeFormatter.ofPattern("HH:mm")
        val localData =
            startTime.value.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return formatter.format(localData)
    }

    fun GetPassedTime() {
        val currentTime = Date.from(Instant.now())
        val diffInMillies: Long = Math.abs(currentTime.time - startTime.value.time)
        val diffInSeconds: Long = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS)
        passedTimeInMinutes = ((diffInSeconds + 59) / 60).toInt()
        passedTimeString = formatPassedTime(diffInSeconds)
    }

    private fun formatPassedTime(time: Long): String {
        val minutes = time / 60
        val hours = time / 3600

        return if (time < 3600) {
            val secondsRemainder = time % 60
            val formattedSeconds = String.format("%02d", secondsRemainder)
            "$minutes:$formattedSeconds mins"
        } else {
            val minutesRemainder = minutes % 60
            val formattedMinutes = String.format("%02d", minutesRemainder)
            "$hours:$formattedMinutes h"
        }
    }
}