package com.example.scoot_scoot.android.ViewModels

import android.annotation.SuppressLint
import android.location.Location
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoot_scoot.android.Data.AutoCompleteResult
import com.example.scoot_scoot.android.Data.ScooterModel
import com.example.scoot_scoot.android.Data.UserManager
import com.example.scoot_scoot.android.Network.GoogleClient
import com.example.scoot_scoot.android.Repository.ScooterRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.concurrent.TimeUnit

class MapViewModel() : ViewModel() {

    private val scooterRepository = ScooterRepository()
    var selectedScooter: MutableState<ScooterModel?> = mutableStateOf(null)

    var useLocation = mutableStateOf(true)
    lateinit var fusedLocationClient: FusedLocationProviderClient


    @OptIn(ExperimentalMaterialApi::class)
    var infoSheetState = ModalBottomSheetState(initialValue = ModalBottomSheetValue.HalfExpanded,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded })

    private val scooterList = MutableStateFlow(listOf<ScooterModel>())
    val scooters: StateFlow<List<ScooterModel>> get() = scooterList

    var riding by mutableStateOf(false)
    var lastLocation: MutableState<Location?> = mutableStateOf(null)
    var kmMode by mutableStateOf(false)
    var passedTimeString by mutableStateOf("")
    var passedTimeInMinutes by mutableIntStateOf(0)
    lateinit var startTime: MutableState<Date>
    var destination = mutableStateOf(AutoCompleteResult("", ""))
    var route by mutableStateOf(GoogleClient.Route(0,""))

    val locationAutofill = mutableStateListOf<AutoCompleteResult>()

    fun searchPlaces(query: String, placesClient: PlacesClient) {
        if (query.length <= 3) {
            return
        }
        val autocompleteSessionToken = AutocompleteSessionToken.newInstance()

        val bias = RectangularBounds.newInstance(
            getCoordinate(
                lastLocation.value!!.latitude,
                lastLocation.value!!.longitude,
                -1000,
                -1000
            )!!,
            getCoordinate(
                lastLocation.value!!.latitude,
                lastLocation.value!!.longitude,
                1000,
                1000
            )!!
        )
        locationAutofill.clear()
        viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest
                .builder()
                .setLocationBias(bias)
                .setSessionToken(autocompleteSessionToken)
                .setQuery(query)
                .build()
            placesClient
                .findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    val predictionLimit = 3

                    locationAutofill += response.autocompletePredictions
                        .take(predictionLimit).map {
                            AutoCompleteResult(
                                it.getFullText(null).toString(),
                                it.placeId
                            )
                        }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    println(it.cause)
                    println(it.message)
                }

        }
    }

    fun getCoordinate(lat0: Double, lng0: Double, dy: Long, dx: Long): LatLng? {
        val lat = lat0 + 180 / Math.PI * (dy / 6378137)
        val lng = lng0 + 180 / Math.PI * (dx / 6378137) / Math.cos(lat0)
        return LatLng(lat, lng)
    }

    fun getStartTime() {
        startTime = mutableStateOf(UserManager.getStartTime())
    }

    @SuppressLint("MissingPermission")
    fun updateLocation(
        cameraState: CameraPositionState
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastLocation = mutableStateOf(location)
                val currentLatLng = LatLng(location.latitude, location.longitude)
                val cameraPosition = CameraPosition.Builder()
                    .target(currentLatLng)
                    .zoom(17f)
                    .build()

                // Set the initial camera position
                cameraState.move(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
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

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val fetchedData = scooterRepository.getAllScooters()
                scooterList.emit(fetchedData)
            }
        }
    }
}