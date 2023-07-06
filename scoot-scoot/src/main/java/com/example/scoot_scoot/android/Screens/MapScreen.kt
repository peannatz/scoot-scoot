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
import com.example.scoot_scoot.android.Data.ScooterModel
import com.example.scoot_scoot.android.Data.UserData
import com.example.scoot_scoot.android.Data.UserManager
import com.example.scoot_scoot.android.ViewModels.MapViewModel
import com.example.scoot_scoot.android.ViewModels.RideViewModel
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object MapScreen {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MapScreen(
        navController: NavController,
        mvm: MapViewModel = viewModel(),
        rvm: RideViewModel = viewModel()
    ) {

        val context = LocalContext.current
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        mvm.fusedLocationClient = fusedLocationClient

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

        LaunchedEffect(rvm.infoSheetState){
            if (!rvm.infoSheetState.isVisible&&!rvm.rideStillRunning()){
                mvm.selectedScooter.value=null
                rvm.clearScooterSelection()
            }
        }


        val coroutineScope = rememberCoroutineScope()


        //TODO: needs changing later, scooter id im be speichern!!!
        LaunchedEffect(Unit){
            if (rvm.rideStillRunning()) {
                coroutineScope.launch { rvm.infoSheetState.show() }
            }
        }

        val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
        val uiSettings by remember {
            mutableStateOf(
                MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
            )
        }

        LaunchedEffect(Unit, mvm.useLocation.value) {
            while (mvm.useLocation.value) {
                mvm.updateLocation(cameraState)
                delay(100)
            }
        }

        LaunchedEffect(cameraState.isMoving) {
            if (cameraState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
                mvm.useLocation.value = false
            }
        }

        LaunchedEffect(mvm.selectedScooter.value) {
            coroutineScope.launch { rvm.infoSheetState.show() }
        }

        GoogleMap(
            modifier = Modifier.fillMaxHeight(),
            cameraPositionState = cameraState,
            properties = mapProperties,
            onMapLoaded = {
                mvm.useLocation.value = true
            },
            onMapClick = {
                coroutineScope.launch { rvm.infoSheetState.hide() }
            },
            uiSettings = uiSettings,
            contentPadding = PaddingValues(top = 120.dp, end = 10.dp),
        )
        {
            val scooter = mvm.scooters.collectAsState().value
            scooter.forEach { item ->
                val position =
                    LatLng(item.location.latitude, item.location.longitude)
                Marker(
                    state = rememberMarkerState(position = position),
                    title = item.name,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                    onClick = { marker ->
                        if (mvm.selectedScooter.value == item) {
                            coroutineScope.launch { rvm.infoSheetState.show() }
                        }
                        mvm.selectedScooter.value = item
                        true
                    }
                )

            }
        }

        MapUi(navController, cameraState)

        mvm.selectedScooter.value?.let {
            MarkerInfoBottomSheet()
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