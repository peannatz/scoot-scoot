package com.example.scoot_scoot.android.ViewModels

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoot_scoot.android.Components.MarkerInfoBottomSheet
import com.example.scoot_scoot.android.Data.AutoCompleteResult
import com.example.scoot_scoot.android.Data.LastLocation
import com.example.scoot_scoot.android.Data.Location
import com.example.scoot_scoot.android.Data.RideModel
import com.example.scoot_scoot.android.Data.RouteModel
import com.example.scoot_scoot.android.Data.ScooterModel
import com.example.scoot_scoot.android.Data.UserData
import com.example.scoot_scoot.android.Data.UserManager
import com.example.scoot_scoot.android.Repository.GoogleRepository
import com.example.scoot_scoot.android.Repository.ScooterRepository
import com.example.scoot_scoot.android.Repository.UserRepository
import com.example.scoot_scoot.android.Service.PriceService.convertToCurrency
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class RideViewModel() : ViewModel() {

    val userRepository = UserRepository()
    val scooterRepository = ScooterRepository()
    val googleRepository = GoogleRepository()
    lateinit var user: UserData

    init{
        CoroutineScope(Dispatchers.IO).launch  { user = userRepository.getUserById(UserManager.getUserId())!! }
    }

    var riding by mutableStateOf(false)
    var kmMode by mutableStateOf(false)
    var passedTimeString by mutableStateOf("")
    var passedTimeInMinutes by mutableIntStateOf(0)
    private var passedTimeInSeconds by mutableLongStateOf(0)
    lateinit var startTime: MutableState<Date?>
    var destination = mutableStateOf(AutoCompleteResult("", ""))
    var startLocation = mutableStateOf(Location(0.00, 0.00))
    var route by mutableStateOf(RouteModel(0, ""))
    var destinationSelection = mutableStateOf(true)
    var originAddress = mutableStateOf("")
    var selectedScooter: MutableState<ScooterModel?> = mutableStateOf(null)

    @OptIn(ExperimentalMaterialApi::class)
    var infoSheetState = ModalBottomSheetState(initialValue = ModalBottomSheetValue.HalfExpanded,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded })


    val latitudeRange = listOf(53.395, 53.729)
    val longitudeRange = listOf(9.712, 10.327)

    fun getStartTime() {
        startTime = mutableStateOf(UserManager.getStartTime())
    }

    fun clearScooterSelection(){
        riding=false
        kmMode=false
        passedTimeString=""
        passedTimeInMinutes=0
        passedTimeInSeconds=0
        startTime.value=null
        destination=mutableStateOf(AutoCompleteResult("", ""))
        startLocation= mutableStateOf(Location(0.00, 0.00))
        route = RouteModel(0, "")
        destinationSelection= mutableStateOf(false)
        originAddress= mutableStateOf("")
        selectedScooter=mutableStateOf(null)
    }

    fun rideStillRunning(): Boolean {
        var runningRide = false
        viewModelScope.launch { runningRide = userRepository.checkForRunningRides(user.id!!) }
        if (runningRide) {
            getCurrentRideInfo()
        }
        return runningRide
    }

    private fun getCurrentRideInfo() {
        getStartTime()
        startLocation.value = user.rides.last().startLocation
        if (user.rides.last().endLocation == null) {
            kmMode = false
            GetPassedTime()
        } else {
            kmMode = true
            GetRouteAndDistance()
        }

        val scooterId = UserManager.getScooterId()
        viewModelScope.launch {
            selectedScooter.value = scooterRepository.getScooterById(scooterId)!!
        }
    }

    fun convertToCurrencyStringMin(): String {
        val cents =
            passedTimeInMinutes * selectedScooter.value!!.tierType.minutePrice
        return convertToCurrency(cents)
    }

    fun convertToCurrencyStringKm(): String {
        val cents = calculatePriceKmInCent()
        return convertToCurrency(cents)
    }

    private fun calculatePriceKmInCent(): Int {
        return route.distanceMeters / 1000 * selectedScooter.value!!.tierType.kilometrePrice
    }

    fun GetRouteAndDistance() {
        viewModelScope.launch {
            val latlng =
                LatLng(startLocation.value.latitude, startLocation.value.longitude)
            val fetchedRoute = GoogleRepository().getRoute(latlng, destination.value.placeId)
            if (fetchedRoute != null) {
                route = fetchedRoute
            }
        }
    }

    fun startRide() {
        UserManager.saveStartTime()
        getStartTime()
        viewModelScope.launch {
            val startLocation = LastLocation.value.value
            originAddress.value =
                googleRepository.getAddressFromLatLng(LastLocation.value.value)
            val newRide = RideModel(
                startTime = UserManager.getStartTime(),
                startLocation = startLocation
            )
            if (kmMode) {
                val destinationLatLng =
                    googleRepository.getPlaceLatLng(destination.value.placeId)
                val endLocation = Location(destinationLatLng.latitude, destinationLatLng.longitude)
                user.rides.last().endLocation = endLocation
                user.rides.last().price = calculatePriceKmInCent()
            }
            user.rides.add(newRide)
            updateRide()
        }
        GetPassedTime()
    }

    @OptIn(ExperimentalMaterialApi::class)
    fun endRide() {
        viewModelScope.launch {
            var endLocation = Location(0.00, 0.00)
            val price: Int
            if (kmMode) {
                price =
                    route.distanceMeters / 1000 * selectedScooter.value!!.tierType.minutePrice
            } else {
                val latitude = Random.nextDouble(latitudeRange[0], latitudeRange[1])
                val longitude = Random.nextDouble(longitudeRange[0], longitudeRange[1])
                endLocation = Location(latitude, longitude)
                price = passedTimeInMinutes * selectedScooter.value!!.tierType.minutePrice
            }
            user.rides.last().apply {
                if (!kmMode) this.endLocation = endLocation
                this.price = price
                this.rideLength = passedTimeInSeconds.toInt()
            }
            updateRide()
            val updatedScooter = selectedScooter.value
            updatedScooter!!.location = endLocation
            scooterRepository.updateScooterById(selectedScooter.value!!.id, updatedScooter)

        }
    }

    fun updateRide() {
        viewModelScope.launch {
            userRepository.updateUser(user.id!!, user)
        }
    }

    fun getAddressFromLastLocation() {
        viewModelScope.launch {
            originAddress.value = googleRepository.getAddressFromLatLng(
                LastLocation.value.value
            )
        }
    }

    fun getFormattedTimeString(): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val localData =
            startTime.value!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return formatter.format(localData)
    }

    fun GetPassedTime() {
        val currentTime = Date.from(Instant.now())
        val diffInMillies: Long = Math.abs(currentTime.time - startTime.value!!.time)
        passedTimeInSeconds = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS)
        passedTimeInMinutes = ((passedTimeInSeconds + 59) / 60).toInt()
        passedTimeString = formatPassedTime(passedTimeInSeconds)
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