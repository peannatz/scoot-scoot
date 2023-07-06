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
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scoot_scoot.android.Components.AutocompleteSearchBox.AutocompleteSearchBox
import com.example.scoot_scoot.android.Data.LastLocation
import com.example.scoot_scoot.android.Data.UserManager
import com.example.scoot_scoot.android.R
import com.example.scoot_scoot.android.Repository.GoogleRepository
import com.example.scoot_scoot.android.ViewModels.MapViewModel
import com.example.scoot_scoot.android.ViewModels.RideViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object MarkerInfoBottomSheet {

    val googleRepository = GoogleRepository()

    lateinit var coroutineScope: CoroutineScope

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MarkerInfoBottomSheet(rvm: RideViewModel = viewModel()) {


        coroutineScope = rememberCoroutineScope()

        ModalBottomSheetLayout(
            sheetState = rvm.infoSheetState,
            scrimColor = Color.Unspecified,
            sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            sheetBackgroundColor = MaterialTheme.colors.background,
            sheetContent = { if (!rvm.riding) ScooterInfo() else RideInfo() }
        ) {
        }
    }

    @Composable
    fun RideInfo(rvm: RideViewModel = viewModel()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 40.dp, vertical = 20.dp)
                .fillMaxWidth()
        ) {
            Text(text = rvm.selectedScooter.value!!.name, fontSize = 30.sp)

            if (rvm.kmMode) {
                RidingInfoKmMode()
            } else {
                RidingInfoMinuteMode()
            }
        }
    }

    //TODO: Destination text hübscher machen

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun RidingInfoKmMode(rvm: RideViewModel = viewModel()) {
        if (rvm.destinationSelection.value) {
            Spacer(modifier = Modifier.size(40.dp))

            AutocompleteSearchBox(
                Modifier.fillMaxWidth(0.8f),
                {
                    rvm.startLocation.value = LastLocation.value.value
                    rvm.GetRouteAndDistance()
                },
                LastLocation.value.collectAsState().value
            )

            if (rvm.route.distanceMeters != 0) Text(
                text = rvm.convertToCurrencyStringKm(),
                fontSize = 40.sp
            )
            Button(onClick = {
                rvm.destinationSelection.value = false
                rvm.startRide()
            })
            {
                Text(text = "Go")
            }
        } else {
            rvm.getAddressFromLastLocation()
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

            //Fahrtpreis
            Text(
                text = rvm.convertToCurrencyStringKm(),
                style = TextStyle(fontSize = 30.sp),
                modifier = Modifier.padding(20.dp)
            )

            Button(onClick = {
                rvm.endRide()
                coroutineScope.launch { rvm.infoSheetState.hide() }
            }) {
                Text(text = "Stop scooting pls")
            }
        }
    }


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun RidingInfoMinuteMode(rvm: RideViewModel = viewModel()) {

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
            text = rvm.convertToCurrencyStringMin(),
            style = TextStyle(fontSize = 30.sp),
            modifier = Modifier.padding(20.dp)
        )

        Button(onClick = {
            rvm.endRide()
            coroutineScope.launch { rvm.infoSheetState.hide() }
        }) {
            Text(text = "Stop scooting pls")
        }

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
                            rvm.startRide()
                        }
                        rvm.selectedScooter = mvm.selectedScooter
                        UserManager.saveScooterId(rvm.selectedScooter.value!!.id)
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
}

