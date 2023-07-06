package com.example.scoot_scoot.android.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scoot_scoot.android.Components.AutocompleteSearchBox.AutocompleteSearchBox
import com.example.scoot_scoot.android.Data.Location
import com.example.scoot_scoot.android.Data.RideModel
import com.example.scoot_scoot.android.Data.UserManager
import com.example.scoot_scoot.android.R
import com.example.scoot_scoot.android.Repository.GoogleRepository
import com.example.scoot_scoot.android.Repository.ScooterRepository
import com.example.scoot_scoot.android.Repository.UserRepository
import com.example.scoot_scoot.android.Service.PriceService.convertToCurrencyStringKm
import com.example.scoot_scoot.android.ViewModels.MapViewModel
import com.example.scoot_scoot.android.ViewModels.RideViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

object MarkerInfoBottomSheet {

    val userRepository = UserRepository()
    val googleRepository = GoogleRepository()
    val scooterRepository = ScooterRepository()

    lateinit var coroutineScope: CoroutineScope

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MarkerInfoBottomSheet(mvm: MapViewModel = viewModel(), rvm: RideViewModel = viewModel()) {
        val modalSheetState = mvm.infoSheetState
        coroutineScope = rememberCoroutineScope()

        ModalBottomSheetLayout(
            sheetState = modalSheetState,
            scrimColor = Color.Unspecified,
            sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            sheetBackgroundColor = MaterialTheme.colors.background,
            sheetContent = { if (!rvm.riding) ScooterInfo() else RideInfo() }
        ) {
        }
    }

    @Composable
    fun RideInfo(mvm: MapViewModel = viewModel(), rvm: RideViewModel = viewModel()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 20.dp)
                .fillMaxWidth()
        ) {
            Text(text = mvm.selectedScooter.value!!.name, fontSize = 30.sp)

            if (rvm.kmMode) {
                RidingInfoKmMode()
            } else {
                RidingInfoMinuteMode()
            }
        }
    }

    fun GetRouteAndDistance(mvm: MapViewModel, rvm: RideViewModel) {
        mvm.viewModelScope.launch {
            val latlng =
                LatLng(mvm.lastLocation.value!!.latitude, mvm.lastLocation.value!!.longitude)
            val route = GoogleRepository().getRoute(latlng, rvm.destination.value.placeId)
            if (route != null) {
                rvm.route = route
            }
        }
    }

    //TODO: Destination text hübscher machen

    @Composable
    fun RidingInfoKmMode(mvm: MapViewModel = viewModel(), rvm: RideViewModel = viewModel()) {
        if (rvm.destinationSelection.value) {
            Spacer(modifier = Modifier.size(40.dp))

            AutocompleteSearchBox(
                Modifier.fillMaxWidth(0.8f),
                { GetRouteAndDistance(mvm, rvm) },
                mvm.lastLocation.value!!
            )
            if (rvm.route.distanceMeters != 0) Text(
                text = convertToCurrencyStringKm(
                    rvm.route.distanceMeters,
                    mvm.selectedScooter.value!!.tierType.minutePrice
                ),
                fontSize = 40.sp
            )
            Button(onClick = {
                rvm.destinationSelection.value=false
                startRide(mvm, rvm)
            })
            {
                Text(text = "Go")
            }
        } else {
            rvm.getAddressFromLastLocation(mvm)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(
                        text = "Origin",
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = rvm.originAddress.value,
                        fontSize = 15.sp,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(0.5f)

                ) {
                    Text(
                        text = "Destination",
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(
                        text = rvm.destination.value.address,
                        fontSize = 15.sp,
                    )
                }
            }


        }

        //Fahrtpreis
        Text(
            text = convertToCurrencyStringKm(
                rvm.route.distanceMeters,
                mvm.selectedScooter.value!!.tierType.minutePrice
            ),
            style = TextStyle(fontSize = 30.sp),
            modifier = Modifier.padding(20.dp)
        )

        Button(onClick = {
            endRide(rvm, mvm)
        }) {
            Text(text = "Stop scooting pls")
        }

    }


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun RidingInfoMinuteMode(rvm: RideViewModel = viewModel(), mvm: MapViewModel = viewModel()) {

        LaunchedEffect(Unit) {
            while (rvm.riding) {
                delay(1000)
                rvm.GetPassedTime()
            }
        }
        //Infos zur Fahrt: Startzeit und Dauer

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(
                text = "Start Time",
            )
            Text(
                text = "Time Passed",
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = rvm.getFormattedTimeString(),
                fontSize = 15.sp,
            )
            Text(
                text = rvm.passedTimeString,
                fontSize = 15.sp,
            )
        }

        //Fahrtpreis
        Text(
            text = convertToCurrencyStringMin(rvm, mvm),
            style = TextStyle(fontSize = 30.sp),
            modifier = Modifier.padding(20.dp)
        )

        Button(onClick = {

        }) {
            Text(text = "Stop scooting pls")
        }

    }

    fun convertToCurrencyStringMin(rvm: RideViewModel, mvm: MapViewModel): String {
        val euros =
            rvm.passedTimeInMinutes * mvm.selectedScooter.value!!.tierType.kilometrePrice / 100.0
        val formattedString = String.format("%.2f", euros)
        return "$formattedString€"
    }

    @Composable
    fun ScooterInfo(mvm: MapViewModel = viewModel(), rvm: RideViewModel = viewModel()) {

        val glowingTextStyle = TextStyle(
            shadow = Shadow(
                color = MaterialTheme.colors.onSecondary,
                blurRadius = 20f
            )
        )
        val regularTextStyle = TextStyle()

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
                Text(
                    text = "minute price",
                    style = if (!rvm.kmMode) glowingTextStyle else regularTextStyle
                )
                Text(
                    text = "km price",
                    style = if (rvm.kmMode) glowingTextStyle else regularTextStyle
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${mvm.selectedScooter.value!!.tierType.minutePrice} ct",
                    fontSize = 15.sp,
                    style = if (!rvm.kmMode) glowingTextStyle else regularTextStyle
                )
                Text(
                    text = "${mvm.selectedScooter.value!!.tierType.kilometrePrice} ct",
                    fontSize = 15.sp,
                    style = if (rvm.kmMode) glowingTextStyle else regularTextStyle
                )
            }

            val name = "s" + mvm.selectedScooter.value!!.id.toString()
            val context = LocalContext.current
            val drawableId1 = R.drawable.s0
            /*            val drawableId = remember(name) {
                            context.resources.getIdentifier(
                                name,
                                "drawable",
                                context.packageName
                            )
                        }*/

            Image(
                painter = painterResource(drawableId1), contentDescription = "",
                modifier = Modifier
                    .fillMaxHeight(0.3f)
                    .padding(10.dp)
            )
            Box(Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        if (!rvm.kmMode) {
                            startRide(mvm, rvm)
                        }
                        rvm.riding = true
                    },
                    Modifier.align(Alignment.Center)
                ) {
                    Text(text = "Let's Scoot")
                }
                Column(
                    Modifier
                        .align(Alignment.CenterEnd)
                        .offset(20.dp, (-20).dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Current Mode:")
                    Text(text = if (rvm.kmMode) "km" else "minute")
                    Switch(
                        checked = rvm.kmMode,
                        onCheckedChange = { _value -> rvm.kmMode = _value })
                }
            }
        }
    }

    fun startRide(mvm: MapViewModel, rvm: RideViewModel) {
        UserManager.saveStartTime()
        rvm.getStartTime()
        rvm.viewModelScope.launch {
            val userId = UserManager.getUserId()
            val user = userRepository.getUserById(userId)!!
            val startLocation = Location(
                mvm.lastLocation.value!!.latitude,
                mvm.lastLocation.value!!.longitude
            )
            val originAddress = googleRepository.getAddressFromLatLng(
                Location(
                    mvm.lastLocation.value!!.latitude,
                    mvm.lastLocation.value!!.longitude
                )
            )
            println(originAddress)
            val newRide = RideModel(
                startTime = UserManager.getStartTime(),
                startLocation = startLocation
            )
            user.rides.add(newRide)
            userRepository.updateUser(userId, user)
        }
        rvm.GetPassedTime()
    }

    //TODO: evtl popup nach ride ende?

    @OptIn(ExperimentalMaterialApi::class)
    fun endRide(rvm: RideViewModel, mvm: MapViewModel) {
        rvm.viewModelScope.launch {
            val userId = UserManager.getUserId()
            val user = userRepository.getUserById(userId)!!
            val endLocation: Location
            val price: Int
            if (rvm.kmMode) {
                val destinationLatLng =
                    googleRepository.getPlaceLatLng(rvm.destination.value.placeId)
                endLocation = Location(destinationLatLng.latitude, destinationLatLng.longitude)
                price =
                    rvm.route.distanceMeters / 1000 * mvm.selectedScooter.value!!.tierType.minutePrice
            } else {
                val latitude = Random.nextDouble(mvm.latitudeRange[0], mvm.latitudeRange[1])
                val longitude = Random.nextDouble(mvm.longitudeRange[0], mvm.longitudeRange[1])
                endLocation = Location(latitude, longitude)
                price = rvm.passedTimeInMinutes * mvm.selectedScooter.value!!.tierType.minutePrice
            }
            user.rides.last().apply {
                this.endLocation = endLocation
                this.price = price
                this.rideLength = rvm.passedTimeInSeconds.toInt()
            }
            userRepository.updateUser(userId, user)
            val updatedScooter = mvm.selectedScooter.value
            updatedScooter!!.location = endLocation
            println()
            scooterRepository.updateScooterById(mvm.selectedScooter.value!!.id, updatedScooter)

        }
        coroutineScope.launch { mvm.infoSheetState.hide() }
    }
}

