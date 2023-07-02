package com.example.scoot_scoot.android.Screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scoot_scoot.android.R
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

object AlcoholTest {

    val firstCardVisible = mutableStateOf(true)
    val secondCardVisible = mutableStateOf(false)
    val thirdCardVisible = mutableStateOf(false)
    val fourthCardVisible = mutableStateOf(false)
    val countdownVisible = mutableStateOf(false)
    val testPassed = mutableStateOf(false)
    val testFailed = mutableStateOf(false)
    val countDown = mutableIntStateOf(5)

    @Composable
    fun AlcoholTest(navController: NavController) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AnimatedVisibility(visible = firstCardVisible.value,
                enter = slideInHorizontally(animationSpec = tween(durationMillis = 40)) { fullWidth ->
                    fullWidth
                },
                exit = slideOutHorizontally(
                    animationSpec =
                    tween(durationMillis = 1)
                ) { fullWidth ->
                    -fullWidth / 3
                }) {
                SwipableCard(
                    content = "Don't drink and drive", imageId = R.drawable.crash, firstCardVisible,
                    secondCardVisible
                )
            }

            AnimatedVisibility(
                visible = secondCardVisible.value,
                enter = slideInHorizontally(animationSpec = tween(durationMillis = 40)) { fullWidth ->
                    fullWidth
                },
                exit = slideOutHorizontally(
                    animationSpec =
                    tween(durationMillis = 1)
                ) { fullWidth ->
                    -fullWidth / 3
                }) {
                SwipableCard(
                    content = "After this instruction a countdown starts",
                    imageId = R.drawable.confused,
                    secondCardVisible, thirdCardVisible
                )
            }

            AnimatedVisibility(
                visible = thirdCardVisible.value,
                enter = slideInHorizontally(animationSpec = tween(durationMillis = 40)) { fullWidth ->
                    fullWidth
                },
                exit = slideOutHorizontally(
                    animationSpec =
                    tween(durationMillis = 1)
                ) { fullWidth ->
                    -fullWidth / 3
                }) {
                SwipableCard(
                    content = "Continue counting in your head \n and tap the screen when the \n counter reaches 0.",
                    imageId = R.drawable.count, thirdCardVisible, fourthCardVisible
                )
            }

            AnimatedVisibility(
                visible = fourthCardVisible.value,
                enter = slideInHorizontally(animationSpec = tween(durationMillis = 40)) { fullWidth ->
                    fullWidth
                },
                exit = slideOutHorizontally(
                    animationSpec =
                    tween(durationMillis = 1)
                ) { fullWidth ->
                    -fullWidth / 3
                }) {
                SwipableCard(
                    content = "If you fail 3 times, \nyour account will be blocked for 2 hours",
                    imageId = R.drawable.sad, fourthCardVisible, countdownVisible
                )
            }
            AnimatedVisibility(visible = countdownVisible.value, enter = fadeIn()) {
                CountdownCard()
            }
            AnimatedVisibility(visible = testFailed.value, enter = fadeIn()) {
                FailedTest()
            }

            AnimatedVisibility(visible = testPassed.value, enter = fadeIn()) {
                PassedTest(navController)
            }
        }
    }

    //TODO fail und win einbauen :)

    @Composable
    fun CountdownCard() {
        LaunchedEffect(Unit) {
            countdownTimer()
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable {
                countdownVisible.value = false
                if (countDown.value != 0) testFailed.value = true else testPassed.value = true
            }
        ) {
            Text(
                text = countDown.value.toString(), Modifier.align(Alignment.Center),
                style = TextStyle(
                    fontSize = 100.sp,
                    color = if (countDown.value < 4) Color.Transparent else MaterialTheme.colors.onBackground
                )
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun SwipableCard(
        content: String,
        imageId: Int,
        currentVisibility: MutableState<Boolean>,
        nextVisibility: MutableState<Boolean>
    ) {

        val squareSize = 600.dp

        val swipeableState = rememberSwipeableState(0)
        val sizePx = with(LocalDensity.current) { squareSize.toPx() }
        val anchors = mapOf(0f to 1, sizePx to 0)

        if (swipeableState.currentValue == 1) {
            currentVisibility.value = false
            nextVisibility.value = true

        }


        Box(
            Modifier
                .fillMaxSize()
                .swipeable(
                    state = swipeableState,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal,
                    anchors = anchors
                )
                .offset {
                    IntOffset(
                        (swipeableState.offset.value - sizePx).roundToInt(),
                        0
                    )
                }
        ) {
            Box(
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.9f)
                    .background(
                        color = MaterialTheme.colors.onPrimary,
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Image(
                    painter = painterResource(id = imageId), contentDescription = null,
                    Modifier
                        .align(Alignment.TopCenter)
                        .size(400.dp)
                )
                Text(
                    text = content,
                    Modifier
                        .align(Alignment.Center)
                        .offset(y = (100).dp), textAlign = TextAlign.Center
                )
                Text(
                    text = "Swipe :)", modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-40).dp)
                )
            }
        }
    }

    @Composable
    private fun PassedTest(navController: NavController) {
        Box(Modifier.fillMaxSize()) {
            Text(
                text = "You are not drunk. Congrats.", Modifier.align(Alignment.Center),
                style = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        LaunchedEffect(Unit) {
            navigateBackToMap(navController)
        }
    }

    //TODO: remove hard coded tries when userManager is implemented
    @Composable
    private fun FailedTest() {
        Box(
            Modifier
                .fillMaxSize()) {
            Text(
                text = "Oh no. You missed. \n You're either drunk or just terrible at this. \n You've got 2 more tries.",
                Modifier.align(Alignment.Center),
                style = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
        LaunchedEffect(Unit) {
            restartTest()
        }
    }

    private suspend fun restartTest() {
        delay(3000)
        testFailed.value=false
        countdownVisible.value=true
        countDown.value = 5
    }

    private suspend fun navigateBackToMap(navController: NavController) {
        delay(1000)
        navController.navigate(Screens.Map)
    }

    private suspend fun countdownTimer() {
        for (i in 1..6) {
            delay(1000)
            countDown.value = countDown.value.minus(1)
        }
    }
}