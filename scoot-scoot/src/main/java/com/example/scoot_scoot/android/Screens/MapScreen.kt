package com.example.scoot_scoot.android.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scoot_scoot.android.Components.MapUi.MapUi
import com.example.scoot_scoot.android.Components.MarkerInfoBottomSheet.MarkerInfoBottomSheet
import com.example.scoot_scoot.android.Data.Location
import com.example.scoot_scoot.android.Data.ScooterModel
import com.example.scoot_scoot.android.ViewModels.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

object MapScreen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MapScreen(navController: NavController, mvm: MapViewModel = viewModel()) {

        val context = LocalContext.current
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        val lng = LatLng(53.9, 9.00)
        val cameraState by remember {
            mutableStateOf(
                CameraPositionState(
                    CameraPosition.fromLatLngZoom(
                        lng,
                        17f
                    )
                )
            )
        }
        mvm.fusedLocationClient = fusedLocationClient

        val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
        val uiSettings by remember {
            mutableStateOf(
                MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
            )
        }

        LaunchedEffect(mvm.useLocation.value) {
            //delay()
            if (mvm.useLocation.value) {
                updateLocation(fusedLocationClient, cameraState)
            }
        }

        var coroutineScope = rememberCoroutineScope()

        LaunchedEffect(cameraState.isMoving) {
            if (cameraState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
                mvm.useLocation.value = false
            }
        }


        GoogleMap(
            modifier = Modifier.fillMaxHeight(),
            cameraPositionState = cameraState,
            properties = mapProperties,
            onMapLoaded = {
                mvm.useLocation.value = true
                updateLocation(fusedLocationClient, cameraState)
            },
            uiSettings = uiSettings,
            contentPadding = PaddingValues(top = 120.dp, end = 10.dp),
        )
        {
            val scooter1 = mvm.scooters.collectAsState().value
            scooter1.forEach { item ->
                val position =
                    LatLng(item.location.latitude.toDouble(), item.location.longitude.toDouble())
                Marker(
                    state = rememberMarkerState(position = position),
                    title = item.name,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                    onClick = selectScooter(mvm,item)
                )

            }
        }
        MapUi(navController, mvm, fusedLocationClient, cameraState)

        mvm.selectedScooter.value?.let { marker ->
            MarkerInfoBottomSheet({ mvm.selectedScooter.value = null }, mvm)
            coroutineScope.launch { mvm.infoSheetState.show() }
        }

        //CurrenRideBottomSheet(mvm)
    }

    private fun selectScooter(mvm: MapViewModel, scooterModel: ScooterModel): (Marker) -> Boolean = { marker ->
        mvm.selectedScooter.value = scooterModel
        true
    }

    @SuppressLint("MissingPermission")
    fun updateLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        cameraState: CameraPositionState
    ) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
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

    val scooterSampleData: List<ScooterModel> = listOf(
        ScooterModel(
            id = 1,
            name = "Scooter McScootface",
            battery = 75,
            available = true,
            location = Location(
                id = 1,
                latitude = 53.5511f,
                longitude = 9.9937f
            ),
            "BASIC"

        ),
        ScooterModel(
            id = 2,
            name = "Electric Glide",
            battery = 90,
            available = true,
            location =
            Location(
                id = 2,
                latitude = 53.5532f,
                longitude = 9.9986f
            ),
            "BASIC"
        ),

        ScooterModel(
            id = 3,
            name = "Vroominator",
            battery = 45,
            available = false,
            location =
            Location(
                id = 3,
                latitude = 53.5503f,
                longitude = 10.0006f
            ),
            "PREMIUM"
        )
    )
}