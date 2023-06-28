package com.example.scoot_scoot.android

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

object SplashScreen {
    @Composable
    fun SplashScreen(navController: NavController) = Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val isVisible = remember { mutableStateOf(true) }
            AnimatedVisibility(
                visible = isVisible.value,
                exit = slideOut(targetOffset = { IntOffset(1200, 0) })
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_scooter),
                    contentDescription = "",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxHeight(0.4f)
                        .padding(40.dp),
                )
            }
            Text(text = "Let's get Scooting", fontSize = 40.sp)
            //TODO: make text fade out and rename wait function
            LaunchedEffect(Unit) {
                wait(navController, isVisible)
            }

        }
    }

    suspend fun wait(navController: NavController, isVisible: MutableState<Boolean>) {
        delay(500)
        isVisible.value = false;
        delay(500)
        navController.navigate(Screens.Dashboard)
    }
}