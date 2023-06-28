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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.maps.android.compose.GoogleMap
import java.math.BigDecimal

object MapView {
    @Composable
    fun MapView(navController: NavController) {
        val contextForToast = LocalContext.current.applicationContext
        val visibleBottom = remember { mutableStateOf(false) }
        if (Build.DEVICE.contains("emu")) {
            MockMapFunctionality(onButtonClick = { visibleBottom.value = true },
                onMapClick = { visibleBottom.value = false })
            RenderMapUi()
            if (visibleBottom.value) {
                OpenBottomSheet(visibleBottom)
            }
        } else {
            GoogleMap(modifier = Modifier.fillMaxHeight(),
                onMapClick = { println("CLICK")})
        }
        //TODO: take this and move it to another composable on up also move the other stuff back to render ui
    }
}

@Composable
fun OpenBottomSheet(visible: MutableState<Boolean>) {
    val model: SharedViewModel = viewModel();
    Box() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight(0.3f)
                .fillMaxWidth(0.8f)
                .offset(y = (-30).dp)
                .background(MaterialTheme.colors.background)
                .padding(20.dp)
        ) {
            //TODO: On tap schließen, zoomen und swipen aber möglich?
            Button(
                onClick = { visible.value = false },
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .align(Alignment.End)
                    .offset((-10).dp, 10.dp)
                    .size(40.dp, 40.dp)
            )
            {
                Image(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close Scooter Info"
                )

            }
            Text(model.selectedScooter.description)
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
fun MockMapFunctionality(onButtonClick: () -> Unit, onMapClick: () -> Unit) {

    val scooters = arrayOf(
        Scooter(0, "ScooterBoi", BigDecimal(0.4), BigDecimal(0.3)),
        Scooter(1, "ScootyMcScootface", BigDecimal(0.5), BigDecimal(0.4)),
        Scooter(2, "iScoot", BigDecimal(0.6), BigDecimal(0.5)),
    )

    Image(
        painter = painterResource(R.drawable.static_map),
        contentDescription = "",
        modifier = Modifier
            .fillMaxHeight()
            .clickable{ println("click") },
        contentScale = ContentScale.FillHeight,
    )
    LazyColumn(verticalArrangement = Arrangement.Center) {
        items(scooters) { scooter ->
            DisplayScooterData(
                scooter = scooter, onButtonClick = onButtonClick
            )
        }
    }
}

@Composable
fun DisplayScooterData(scooter: Scooter, onButtonClick: () -> Unit) {
    Row() {
        //Der text ist nicht sichtbar, aber so sieht das gemockt nicer aus
        Text(text = scooter.description)
        val model: SharedViewModel = viewModel();
        Button(
            onClick = {
                model.UpdateSelectedScooter(scooter)
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