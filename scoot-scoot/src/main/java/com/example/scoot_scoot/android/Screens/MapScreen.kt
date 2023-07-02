package com.example.scoot_scoot.android.Screens

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.scoot_scoot.android.Data.Location
import com.example.scoot_scoot.android.R
import com.example.scoot_scoot.android.Data.ScooterModel
import com.example.scoot_scoot.android.ViewModels.MapViewModel
import com.example.scoot_scoot.android.ViewModels.SharedViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object MapScreen {

    private lateinit var coroutineScope: CoroutineScope
    private var updateScooterInfo = false


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MapScreen(navController: NavController, mvm: MapViewModel = viewModel()) {

        var bottomSheetContent: (@Composable () -> Unit)? by remember {
            mutableStateOf(null)
        }
        coroutineScope = rememberCoroutineScope()

        val toggleBottomSheet: () -> Unit = {
            if (updateScooterInfo) {
                bottomSheetContent = { UpdateScooterInfoInBottomSheet(mvm) }
            }
            if (mvm.sheetState.isVisible && !updateScooterInfo) {
                coroutineScope.launch { mvm.sheetState.hide() }
            } else {
                coroutineScope.launch { mvm.sheetState.show() }
            }
            updateScooterInfo = false
        }



        if (Build.DEVICE.contains("emu")) {
            MockMapFunctionality(mvm,
                onButtonClick = { toggleBottomSheet() })
            RenderMapUi(navController)
            PrepareBottomSheet(mvm, bottomSheetContent)

        } else {
            val sydney = LatLng(-33.852, 151.211)
            val items by mvm.items.collectAsState()

            GoogleMap(modifier = Modifier.fillMaxHeight(),
                onMapClick = { toggleBottomSheet() }) {
                items.forEach { item ->
                    val position=LatLng(item.location.x.toDouble(),item.location.y.toDouble())
                    Marker(
                        state = rememberMarkerState(position = position),
                        title = item.name,
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun PrepareBottomSheet(
        mvm: MapViewModel,
        bottomSheetContent: @Composable (() -> Unit)?
    ) {
        val modalSheetState = mvm.sheetState
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
    fun UpdateScooterInfoInBottomSheet(mvm: MapViewModel) {
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
    fun RenderMapUi(navController: NavController) {
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


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MockMapFunctionality(
        mvm: MapViewModel,
        onButtonClick: () -> Unit
    ) {

        Box(modifier = Modifier.fillMaxHeight()) {
            val scooters = arrayOf(
                ScooterModel(0, "ScooterBoi", 50, false, Location(1, 3f, 5f)),
                ScooterModel(1, "ScootyMcScootface", 40, true, Location(2, 4f, 9f)),
                ScooterModel(2, "iScoot", 60, true, Location(3, 5f, 9f)),
                ScooterModel(3, "Scootie Doo", 60, true, Location(4, 6f, 9f)),
            )

            Image(
                painter = painterResource(R.drawable.static_map),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { coroutineScope.launch { mvm.sheetState.hide() } },
                contentScale = ContentScale.FillHeight,
            )
            LazyColumn(verticalArrangement = Arrangement.Center) {
                items(scooters) { scooter ->
                    DisplayScooterData(
                        mvm,
                        scooter = scooter, onButtonClick
                    )
                }
            }
        }
    }

    @Composable
    fun DisplayScooterData(
        mvm: MapViewModel,
        scooter: ScooterModel,
        onButtonClick: () -> Unit
    ) {
        Row() {
            //Der text ist nicht sichtbar, aber so sieht das gemockt nicer aus
            Text(text = scooter.name, Modifier.alpha(0f))
            val model: SharedViewModel = viewModel()
            Button(
                onClick = {
                    updateScooterInfo = true
                    mvm.UpdateSelectedScooter(scooter)
                    onButtonClick()
                },
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(50.dp, 50.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.KeyboardArrowUp, contentDescription = "",
                )
            }
        }
    }

}
