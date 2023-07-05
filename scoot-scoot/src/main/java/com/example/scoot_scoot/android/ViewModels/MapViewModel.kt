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
import com.example.scoot_scoot.android.Data.RouteModel
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
    var lastLocation: MutableState<Location?> = mutableStateOf(null)

    val latitudeRange = listOf(53.395, 53.729)
    val longitudeRange = listOf(9.712, 10.327)

    var useLocation = mutableStateOf(true)
    lateinit var fusedLocationClient: FusedLocationProviderClient


    @OptIn(ExperimentalMaterialApi::class)
    var infoSheetState = ModalBottomSheetState(initialValue = ModalBottomSheetValue.HalfExpanded,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded })

    private val scooterList = MutableStateFlow(listOf<ScooterModel>())
    val scooters: StateFlow<List<ScooterModel>> get() = scooterList




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