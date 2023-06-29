package com.example.scoot_scoot.android.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

object ProfileScreen {
    @Composable
    fun ProfileScreen(navController: NavController) {
        val paddingValuesTop: PaddingValues= PaddingValues(40.dp,30.dp,40.dp,5.dp)
        val paddingValuesBottom: PaddingValues= PaddingValues(40.dp,5.dp,40.dp,30.dp)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth().fillMaxHeight()

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(vertical = 2.dp)
                    .background(
                        MaterialTheme.colors.primary
                    )
            ) {
                Text(
                    text = "Surname",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(paddingValuesTop)
                )
                Text(
                    text = "Uwe",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(paddingValuesBottom),
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(vertical = 2.dp)
                    .background(
                        MaterialTheme.colors.primary
                    )
            ) {
                Text(
                    text = "Name",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(paddingValuesTop)
                )
                Text(
                    text = "Uwington",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(paddingValuesBottom),
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(vertical = 2.dp)
                    .background(
                        MaterialTheme.colors.primary
                    )
            ) {
                Text(
                    text = "Email",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(paddingValuesTop)
                )
                Text(
                    text = "uwe@uwe.de",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(paddingValuesBottom),
                    style = TextStyle(fontSize = 30.sp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(vertical = 2.dp)
                    .background(
                        MaterialTheme.colors.primary
                    )
            ) {
                Text(
                    text = "Birthday",
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(paddingValuesTop)
                )
                Text(
                    text = "01.01.1990",
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(paddingValuesBottom),
                    style = TextStyle(fontSize = 30.sp)
                )
            }
        }
    }
}