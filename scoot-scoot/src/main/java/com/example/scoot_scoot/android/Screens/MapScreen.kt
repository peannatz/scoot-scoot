package com.example.scoot_scoot.android.Screens

import android.annotation.SuppressLint
import android.provider.Settings.Global.getString
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
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
import com.example.scoot_scoot.android.R
import com.example.scoot_scoot.android.ViewModels.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.util.Properties

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

        LaunchedEffect(mvm.useLocation.value, Unit) {
            if (mvm.useLocation.value) {
                mvm.updateLocation(cameraState)
            }
        }

        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(cameraState.isMoving) {
            if (cameraState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
                mvm.useLocation.value = false
            }
        }

        LaunchedEffect(mvm.selectedScooter.value) {
            coroutineScope.launch { mvm.infoSheetState.show() }
        }

        GoogleMap(
            modifier = Modifier.fillMaxHeight(),
            cameraPositionState = cameraState,
            properties = mapProperties,
            onMapLoaded = {
                mvm.useLocation.value = true
                mvm.updateLocation(cameraState)
            },
            onMapClick = {
                coroutineScope.launch { mvm.infoSheetState.hide() }
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
                    onClick = selectScooter(mvm, item)
                )

            }
        }

        MapUi(navController, mvm, fusedLocationClient, cameraState)

        mvm.selectedScooter.value?.let { marker ->
            MarkerInfoBottomSheet(mvm)
        }
    }

    private fun selectScooter(mvm: MapViewModel, scooterModel: ScooterModel): (Marker) -> Boolean =
        { marker ->
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

/*    val scooterSampleData: List<ScooterModel> = listOf(
        ScooterModel(
            id = 1,
            name = "Scooter McScootface",
            battery = 75,
            available = true,
            location = Location(
                latitude = 53.5511,
                longitude = 9.9937
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
                latitude = 53.5532,
                longitude = 9.9986
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
                latitude = 53.5503,
                longitude = 10.0006
            ),
            "PREMIUM"
        )
    )*/
}