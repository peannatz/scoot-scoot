package com.example.scoot_scoot.android

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.maps.android.compose.GoogleMap
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

object MapScreen {
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MapScreen(navController: NavController) {
        var bottomSheetContent: (@Composable () -> Unit)? by remember {
            mutableStateOf(null)
        }
        val coroutineScope = rememberCoroutineScope()
        val model: SharedViewModel = viewModel()

        val toggleBottomSheet: () -> Unit = {
            if (model.updateScooterInfo){
                bottomSheetContent = { UpdateScooterInfoInBottomSheet(model = model) }
            }
            if (model.sheetState.isVisible && !model.updateScooterInfo) {
                coroutineScope.launch { model.sheetState.hide() }
            } else {
                coroutineScope.launch { model.sheetState.show() }
            }
            model.updateScooterInfo=false
        }

        if (Build.DEVICE.contains("emu")) {
            MockMapFunctionality(
                onMapClick = { toggleBottomSheet() },
                onButtonClick = { toggleBottomSheet() })
            RenderMapUi()
            PrepareBottomSheet(bottomSheetContent)

        } else {
            GoogleMap(modifier = Modifier.fillMaxHeight(),
                onMapClick = { toggleBottomSheet() })
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PrepareBottomSheet(
    bottomSheetContent: @Composable() (() -> Unit)?
) {
    val model: SharedViewModel = viewModel()
    val modalSheetState = model.sheetState
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        sheetBackgroundColor = MaterialTheme.colors.background,
        scrimColor = Color.Unspecified,
        sheetContent = {
            bottomSheetContent?.let { it() }
        }
    ) {
    }
}

@Composable
fun UpdateScooterInfoInBottomSheet(model: SharedViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 40.dp, vertical = 20.dp)
            .fillMaxWidth()
    ) {
        Text(text = model.selectedScooter.description, fontSize = 30.sp)
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
                text = model.selectedScooter.priceMin.setScale(2, RoundingMode.HALF_EVEN)
                    .toString(),
                fontSize = 15.sp
            )
            Text(
                text = model.selectedScooter.priceKm.setScale(2, RoundingMode.HALF_EVEN).toString(),
                fontSize = 15.sp
            )
        }
        val name = "s" + model.selectedScooter.id.toString()
        val context = LocalContext.current
        val drawableId = remember(name) {
            context.resources.getIdentifier(
                name,
                "drawable",
                context.packageName
            )
        }

        Image(
            painter = painterResource(drawableId), contentDescription = "",
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .padding(10.dp)
        )

        Button(onClick = { println("Let's scoot") }) {
            Text(text = "Let's Scoot")
        }
    }
}

@Composable
fun RenderMapUi() {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        FloatingActionButton(
            onClick = {
                //OnClick Method
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


@Composable
fun MockMapFunctionality(
    onMapClick: () -> Unit,
    onButtonClick: () -> Unit
) {

    Box(modifier = Modifier.fillMaxHeight()) {
        val scooters = arrayOf(
            Scooter(0, "ScooterBoi", BigDecimal(0.5), BigDecimal(0.3)),
            Scooter(1, "ScootyMcScootface", BigDecimal(0.6), BigDecimal(0.4)),
            Scooter(2, "iScoot", BigDecimal(0.7), BigDecimal(0.5)),
            Scooter(3, "Scootie Doo", BigDecimal(0.9), BigDecimal(0.6)),
        )

        Image(
            painter = painterResource(R.drawable.static_map),
            contentDescription = "",
            modifier = Modifier
                .fillMaxHeight()
                .clickable { onMapClick() },
            contentScale = ContentScale.FillHeight,
        )
        LazyColumn(verticalArrangement = Arrangement.Center) {
            items(scooters) { scooter ->
                DisplayScooterData(
                    scooter = scooter, onButtonClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DisplayScooterData(
    scooter: Scooter,
    onButtonClick: () -> Unit
) {
    Row() {
        //Der text ist nicht sichtbar, aber so sieht das gemockt nicer aus
        Text(text = scooter.description, Modifier.alpha(0f))
        val model: SharedViewModel = viewModel()
        Button(
            onClick = {
                model.UpdateSelectedScooter(scooter)
                model.updateScooterInfo=true
                onButtonClick()
            },
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(50.dp, 50.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowUp, contentDescription = "",
            )
        }
    }
}