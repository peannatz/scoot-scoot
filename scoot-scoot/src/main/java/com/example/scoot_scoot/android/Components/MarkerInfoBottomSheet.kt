package com.example.scoot_scoot.android.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scoot_scoot.android.Data.UserManager
import com.example.scoot_scoot.android.R
import com.example.scoot_scoot.android.ViewModels.MapViewModel
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.concurrent.TimeUnit

object MarkerInfoBottomSheet {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MarkerInfoBottomSheet(onClose: () -> Unit, mvm: MapViewModel) {
        val modalSheetState = mvm.infoSheetState

        ModalBottomSheetLayout(
            sheetState = modalSheetState,
            scrimColor = Color.Unspecified,
            sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            sheetBackgroundColor = MaterialTheme.colors.background,
            sheetContent = { if (!mvm.riding) ScooterInfo(mvm) else RidingInfoMinuteMode(mvm) }
        ) {
        }
    }

    @Composable
    fun RidingInfoMinuteMode(mvm: MapViewModel) {

        LaunchedEffect(Unit){
            while (mvm.riding){
                delay(30000)
                mvm.GetPassedTime()
            }
        }

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
                    text = mvm.getFormattedTimeString(),
                    fontSize = 15.sp,
                )
                Text(
                    text = mvm.passedTime,
                    fontSize = 15.sp,
                )
            }
            //TODO: Price!!
            //Text(text = )

        }
    }


    @Composable
    fun ScooterInfo(mvm: MapViewModel) {

        val glowingTextStyle = TextStyle(
            shadow = Shadow(
                color = MaterialTheme.colors.onSecondary,
                blurRadius = 20f
            )
        )
        val regularTextStyle = TextStyle()
        var pricePerKm by remember { mutableStateOf(false) }

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
                    style = if (!pricePerKm) glowingTextStyle else regularTextStyle
                )
                Text(
                    text = "km price",
                    style = if (pricePerKm) glowingTextStyle else regularTextStyle
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    //text = model.selectedScooter.priceMin.setScale(2, RoundingMode.HALF_EVEN)
                    text = mvm.selectedScooter.value!!.tier
                        .toString(),
                    fontSize = 15.sp,
                    style = if (!pricePerKm) glowingTextStyle else regularTextStyle
                )
                Text(
                    //text = model.selectedScooter.priceKm.setScale(2, RoundingMode.HALF_EVEN)
                    text = "0.2"
                        .toString(),
                    fontSize = 15.sp,
                    style = if (pricePerKm) glowingTextStyle else regularTextStyle
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
            Box(Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        UserManager.saveStartTime()
                        mvm.getStartTime()
                        mvm.GetPassedTime()
                        mvm.riding = true
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
                    Text(text = if (pricePerKm) "km" else "minute")
                    Switch(
                        checked = pricePerKm,
                        onCheckedChange = { _value -> pricePerKm = _value })
                }
            }
        }
    }
}

