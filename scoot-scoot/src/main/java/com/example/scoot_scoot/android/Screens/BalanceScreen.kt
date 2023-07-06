package com.example.scoot_scoot.android.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.scoot_scoot.android.Components.ProfileBottomBar
import com.example.scoot_scoot.android.Data.UserData
import com.example.scoot_scoot.android.Data.UserManager
import com.example.scoot_scoot.android.Repository.UserRepository
import com.example.scoot_scoot.android.Service.PriceService
import kotlinx.coroutines.launch

object BalanceScreen {

    @OptIn(ExperimentalComposeUiApi::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun BalanceScreen(navController: NavController) {
        val userRepository = UserRepository()
        val userId = UserManager.getUserId()
        var user = remember {mutableStateOf(UserData())}
        var userBalance = remember { mutableIntStateOf(0) }
        val coroutineScope = rememberCoroutineScope()
        coroutineScope.launch {
            user = mutableStateOf(userRepository.getUserById(userId)!!)
            userBalance.intValue = user.value.credit
        }
        val openDialog = remember { mutableStateOf(false) }
        var input = remember { mutableStateOf(TextFieldValue("")) }
        val keyboardController = LocalSoftwareKeyboardController.current

        Scaffold(
            bottomBar = {
                BottomAppBar(modifier = Modifier.height(70.dp)) {
                    ProfileBottomBar.ProfileBottomBar(navController)
                }
            }
        ) { paddingValues ->

            Box(
                Modifier
                    .padding(paddingValues)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "Your current Balance:", fontSize = 80.sp, textAlign = TextAlign.Left,
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Text(
                    text = PriceService.convertToCurrency(userBalance.intValue),
                    fontSize = 60.sp,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.align(Alignment.Center)
                )
                Button(
                    onClick = { openDialog.value = true },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Text(text = "Top up Balance")
                }
                if (openDialog.value) {

                    Dialog(
                        onDismissRequest = {
                            // Dismiss the dialog when the user clicks outside the dialog or on the back
                            // button. If you want to disable that functionality, simply use an empty
                            // onCloseRequest.
                            openDialog.value = false
                        },
                    ) {
                        Box(
                            Modifier
                                .clip(RectangleShape)
                                .background(MaterialTheme.colors.onPrimary)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "The Real Paypal",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text("How much would you like to add?")
                                TextField(
                                    value = input.value,
                                    onValueChange = { input.value = it },
                                    Modifier.padding(horizontal = 20.dp),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    keyboardActions = KeyboardActions(
                                        onDone = { keyboardController?.hide() }),
                                )
                                Row() {
                                    Button(onClick = {
                                        user.value.credit += input.value.text.toInt()
                                        coroutineScope.launch {
                                            userRepository.updateUser(
                                                userId,
                                                user.value
                                            )
                                            user.value=userRepository.getUserById(userId)!!
                                        }
                                        openDialog.value = false
                                    }) {
                                        Text(text = "Confirm")
                                    }
                                    Spacer(modifier = Modifier.size(10.dp))
                                    Button(onClick = { openDialog.value = false }) {
                                        Text(text = "Abort")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}