package com.example.scoot_scoot.android.ViewModels

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.example.scoot_scoot.android.Data.LocationData
import com.example.scoot_scoot.android.Data.UserManager
import com.example.scoot_scoot.android.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException


class LocationViewModel(context: Context) : ViewModel() {

    val eyreSquare = LocationData(53.2743394, -9.0514163)
    val location = MutableStateFlow<LocationData>(eyreSquare)
    val currentLocation = MutableStateFlow<LocationData>(eyreSquare)
    var useLocation = MutableStateFlow<Boolean>(true)
    var useLocation1 = mutableStateOf(true)


    val cameraPosition = MutableStateFlow<LocationData?>(null)
    private val zoomLevel = MutableStateFlow<Float>(15.0f)


    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun awaitLastLocation(
        fusedLocation: FusedLocationProviderClient,
        context: Context
    ): Location =
        suspendCancellableCoroutine { continuation ->
            if (UserManager.getPermissionsStatus()) {
                fusedLocation.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        currentLocation.value = LocationData(location.latitude, location.longitude)
                        continuation.resume(location, onCancellation = {})
                    }
                }.addOnFailureListener { e ->
                    continuation.resumeWithException(e)
                }
            }
        }


}
