package com.example.scoot_scoot.android.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scoot_scoot.android.R
import kotlinx.coroutines.delay

object SplashScreen {

    //TODO: Figure out how to remove androids splash screen
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
            AnimatedVisibility(visible = isVisible.value,
                exit = fadeOut()){
                Text(text = "Let's get Scooting", fontSize = 40.sp)
            }

            LaunchedEffect(Unit) {
                waitForSplashscreenFade(navController, isVisible)
            }

        }
    }

    private suspend fun waitForSplashscreenFade(navController: NavController, isVisible: MutableState<Boolean>) {
        delay(500)
        isVisible.value = false;
        delay(500)
        navController.navigate(Screens.Dashboard)
    }
}