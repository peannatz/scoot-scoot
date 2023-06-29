package com.example.scoot_scoot.android.Screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scoot_scoot.android.Screens.Screens

object Dashboard {
    @Composable
    fun Dashboard(navController: NavController) = Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                "Hi! \n I'm Scooty. \n Let's go!",
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp
            )
            Button(
                onClick = {
                    navController.navigate(Screens.Register)
                },
                Modifier.padding(10.dp)
            )
            {
                Text(
                    text = "Login",
                    fontSize = 30.sp,
                )
            }
        }
    }
}