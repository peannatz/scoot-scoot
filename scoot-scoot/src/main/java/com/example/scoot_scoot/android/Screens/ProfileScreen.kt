package com.example.scoot_scoot.android.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

object ProfileScreen {
    @Composable
    fun ProfileScreen(navController: NavController) {
        //TODO: Add clickability and option to edit and move to own component
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()

        ) {
            UserDataEntry(type = "Surname", data = "Uwe", editable = true)
            UserDataEntry(type = "Name", data = "Uwington", editable = true)
            UserDataEntry(type = "Email", data = "uwe@uwe.de", editable = true)
            UserDataEntry(type = "Birthday", data = "01.01.1990", editable = false)
        }
    }

    @Composable
    fun UserDataEntry(type: String, data: String, editable: Boolean){
        val paddingValuesTop = PaddingValues(40.dp,30.dp,40.dp,5.dp)
        val paddingValuesBottom= PaddingValues(40.dp,5.dp,40.dp,30.dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(vertical = 2.dp)
                .background(
                    MaterialTheme.colors.primary
                )
                .then(if(editable) Modifier.clickable { println("Clicked")} else Modifier)
        ) {
            Text(
                text = type,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(paddingValuesTop)
            )
            Text(
                text = data,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(paddingValuesBottom),
                style = TextStyle(fontSize = 30.sp)
            )
            //TODO:no icon when not clickable
            Icon(
                imageVector= Icons.Default.KeyboardArrowRight,
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(80.dp))
        }
    }
}