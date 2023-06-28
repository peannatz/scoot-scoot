package com.example.scoot_scoot.android

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.maps.android.compose.GoogleMap
import java.math.BigDecimal

object Map {
    @Composable
    fun Map(navController: NavController) {
        val contextForToast = LocalContext.current.applicationContext
        val visibleBottom = remember { mutableStateOf(false) }
        if (Build.DEVICE.contains("emu")) {
            MockMapFunctionality(onButtonClick = { visibleBottom.value = true })
            RenderMapUi()
        } else {
            GoogleMap(modifier = Modifier.fillMaxHeight())
        }
        //TODO: take this and move it to another composable on up also move the other stuff back to render ui
        if (visibleBottom.value) {
            OpenBottomSheet()
        }
    }
}

@Composable
fun OpenBottomSheet() {
    Column(
        Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxWidth(0.8f)
    ) {
        Text("SOMETIMES IM ALONE")
        Text("SOMETIMES IM NOT")
        Text("SOMETIMES IM ALONE")
        Text("HELLO?")
    }
}

@Composable
fun RenderMapUi() {
    Column(
        Modifier.fillMaxSize()
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
fun MockMapFunctionality(onButtonClick: (Scooter) -> Unit) {

    val scooters = arrayOf(
        Scooter(0, "ScooterBoi", BigDecimal(0.4), BigDecimal(0.3)),
        Scooter(1, "ScootyMcScootface", BigDecimal(0.5), BigDecimal(0.4)),
        Scooter(2, "iScoot", BigDecimal(0.6), BigDecimal(0.5)),
    )

    Image(
        painter = painterResource(R.drawable.static_map),
        contentDescription = "",
        modifier = Modifier.fillMaxHeight(),
        contentScale = ContentScale.FillHeight
    )
    LazyColumn(verticalArrangement = Arrangement.Center) {
        items(scooters) { scooter ->
            DisplayScooterData(
                scooter = scooter,
                onButtonClick = onButtonClick
            )
        }
    }
}

@Composable
fun DisplayScooterData(scooter: Scooter, onButtonClick: (Scooter) -> Unit) {
    Row() {
        //Der text ist nicht sichtbar, aber so sieht das gemockt nicer aus
        Text(text = scooter.description)
        val model = SharedViewModel();
        Button(
            onClick = { onButtonClick(scooter) },
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(50.dp, 50.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowUp, contentDescription = "",
            )
        }
    }
}