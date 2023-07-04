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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoot_scoot.android.Data.AutoCompleteResult
import com.example.scoot_scoot.android.Data.ScooterModel
import com.example.scoot_scoot.android.Data.UserManager
import com.example.scoot_scoot.android.Repository.ScooterRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
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

    val locationAutofill = mutableStateListOf<AutoCompleteResult>()

    private var job: Job? = null

    fun searchPlaces(query: String, placesClient: PlacesClient) {
        val autocompleteSessionToken = AutocompleteSessionToken.newInstance()
        locationAutofill.clear()
        viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest
                .builder()
                .setSessionToken(autocompleteSessionToken)
                .setQuery(query)
                .build()
            placesClient
                .findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    locationAutofill += response.autocompletePredictions.map {
                        println(it.placeId)
                        AutoCompleteResult(
                            it.placeId,
                            it.placeId
                        )
                    }
                }
                .addOnFailureListener {
                    println("TEST")
                    it.printStackTrace()
                    println(it.cause)
                    println(it.message)
                }

        }
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