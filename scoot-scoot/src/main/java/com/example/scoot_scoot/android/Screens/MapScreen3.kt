package com.example.scoot_scoot.android.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scoot_scoot.android.Data.LocationData
import com.example.scoot_scoot.android.Data.ScooterModel
import com.example.scoot_scoot.android.R
import com.example.scoot_scoot.android.ViewModels.LocationViewModel
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
import kotlinx.coroutines.delay

object MapScreen3 {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MapScreen3(navController: NavController, mvm: MapViewModel = viewModel()) {

        val context = LocalContext.current
        val lvm = LocationViewModel(context)
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
        val lng = LatLng(53.9, 9.00)

        val modalSheetState = mvm.sheetState
        var bottomSheetContent: (@Composable () -> Unit)? by remember {
            mutableStateOf(null)
        }
        val coroutineScope = rememberCoroutineScope()

        val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
        val uiSettings by remember {
            mutableStateOf(
                MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
            )
        }


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

        val fireCoroutine = remember { mutableStateOf(true) }


        LaunchedEffect(fireCoroutine) {
            //delay()
            if (fireCoroutine.value) {
                updateLocation(fusedLocationClient,cameraState)
            }
        }

        mvm.selectedScooter1.value?.let { marker ->
            MarkerInfoBottomSheet({ mvm.selectedScooter1.value = null }, mvm)
        }



        LaunchedEffect(cameraState.isMoving) {
            if (cameraState.cameraMoveStartedReason == CameraMoveStartedReason.GESTURE) {
                fireCoroutine.value = false
            }
        }


        GoogleMap(
            modifier = Modifier.fillMaxHeight(),
            cameraPositionState = cameraState,
            properties = mapProperties,
            onMapLoaded = {
                //isFiringCoroutine.value = true
                fireCoroutine.value = true
                updateLocation(fusedLocationClient, cameraState)
            },
            uiSettings = uiSettings,
            contentPadding = PaddingValues(top = 120.dp, end = 10.dp),
        )
        {
/*            MapScreen.scooters.forEach { item ->
                val position = LatLng(item.location.x.toDouble(), item.location.y.toDouble())
                Marker(
                    state = rememberMarkerState(position = position),
                    title = item.name,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                    onClick = test1(mvm, item)
                )
            }*/
        }
        RenderMapUi(navController, lvm, fusedLocationClient, cameraState, fireCoroutine)
    }

    @SuppressLint("MissingPermission")
    fun updateLocation(
        fusedLocationProviderClient: FusedLocationProviderClient, cameraState: CameraPositionState
    ) {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                val cameraPosition = CameraPosition.Builder()
                    .target(currentLatLng)
                    .zoom(15f)
                    .build()

                // Set the initial camera position
                cameraState.move(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }
    }

    fun useLocation(lvm: LocationViewModel): () -> Boolean {
        lvm.useLocation.value = true
        return { false }
    }

    fun test1(mvm: MapViewModel, scooterModel: ScooterModel): (Marker) -> Boolean {
        mvm.selectedScooter1.value = scooterModel
        return { marker ->
            true
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MarkerInfoBottomSheet(onClose: () -> Unit, mvm: MapViewModel) {
        val modalSheetState = mvm.sheetState
        ModalBottomSheetLayout(
            sheetState = modalSheetState,
            sheetContent = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 40.dp, vertical = 20.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = mvm.selectedScooter.name, fontSize = 30.sp)
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        Text(text = "minute price")
                        Text(text = "km price")
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            //text = model.selectedScooter.priceMin.setScale(2, RoundingMode.HALF_EVEN)
                            text = "0.5"
                                .toString(),
                            fontSize = 15.sp
                        )
                        Text(
                            //text = model.selectedScooter.priceKm.setScale(2, RoundingMode.HALF_EVEN)
                            text = "0.2"
                                .toString(),
                            fontSize = 15.sp
                        )
                    }
                    val name = "s" + mvm.selectedScooter.id.toString()
                    val context = LocalContext.current
                    val drawableId1 = R.drawable.s0
                    val drawableId = remember(name) {
                        context.resources.getIdentifier(
                            name,
                            "drawable",
                            context.packageName
                        )
                    }

                    Image(
                        painter = painterResource(drawableId1), contentDescription = "",
                        modifier = Modifier
                            .fillMaxHeight(0.3f)
                            .padding(10.dp)
                    )

                    Button(onClick = { println("Let's scoot") }) {
                        Text(text = "Let's Scoot")
                    }
                }
            }
        ) {
            // Your app content
        }
    }

    @Composable
    fun RenderMapUi(
        navController: NavController,
        lvm: LocationViewModel,
        fusedLocationClient: FusedLocationProviderClient,
        cameraState: CameraPositionState,
        fireCoroutine: MutableState<Boolean>
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.Profile)
                },
                shape = RoundedCornerShape(50),
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.End)
                    .offset((-30).dp, 30.dp)
                    .size(75.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(60.dp)
                )
            }

            FloatingActionButton(
                onClick = {
                    updateLocation(fusedLocationClient, cameraState)
                    fireCoroutine.value=true;
                },
                shape = RoundedCornerShape(50),
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.End)
                    .offset((-30).dp)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.LocationOn,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(30.dp)
                )
            }

            var text by remember { mutableStateOf(TextFieldValue("")) }

            TextField(
                value = text,
                onValueChange = { text = it },
                Modifier
                    .fillMaxWidth(0.8f)
                    .offset(y = (-30).dp),
                placeholder = { Text("Destination") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background
                )
            )
        }
    }
}