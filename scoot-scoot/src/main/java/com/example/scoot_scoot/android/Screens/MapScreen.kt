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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
        RenderMapUi(navController, mvm, fusedLocationClient, cameraState)

        mvm.selectedScooter.value?.let { marker ->
            MarkerInfoBottomSheet({ mvm.selectedScooter.value = null }, mvm)
            coroutineScope.launch { mvm.sheetState.show() }
        }

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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MarkerInfoBottomSheet(onClose: () -> Unit, mvm: MapViewModel) {
        val modalSheetState = mvm.sheetState
        ModalBottomSheetLayout(
            sheetState = modalSheetState,
            scrimColor = Color.Unspecified,
            sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            sheetContent = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 40.dp, vertical = 20.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = mvm.selectedScooter.value!!.name, fontSize = 30.sp)
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
                            text = mvm.selectedScooter.value!!.tier
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
                    val name = "s" + mvm.selectedScooter.value!!.id.toString()
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
        }
    }

    @Composable
    fun RenderMapUi(
        navController: NavController,
        mvm: MapViewModel,
        fusedLocationClient: FusedLocationProviderClient,
        cameraState: CameraPositionState,
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
                    mvm.useLocation.value = true;
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