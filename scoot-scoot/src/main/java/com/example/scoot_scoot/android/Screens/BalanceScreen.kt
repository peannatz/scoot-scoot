package com.example.scoot_scoot.android.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scoot_scoot.android.Components.ProfileBottomBar
import com.example.scoot_scoot.android.Service.PriceService.convertToCurrency
import com.example.scoot_scoot.android.ViewModels.BalanceViewModel

object BalanceScreen {

    @OptIn(ExperimentalComposeUiApi::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun BalanceScreen(navController: NavController, bvm: BalanceViewModel = viewModel()) {

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
                    text = "Your current Balance:", fontSize = 80.sp, textAlign = TextAlign.Left, color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(color = MaterialTheme.colors.primary)
                        .padding(40.dp)
                )
                Text(
                    text = convertToCurrency(bvm.userBalance.intValue),
                    fontSize = 60.sp,
                    textAlign = TextAlign.Left,
                    color= MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = 100.dp)
                )
                Button(
                    onClick = { openDialog.value = true },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-40).dp)
                ) {
                    Text(text = "Top up Balance", modifier = Modifier.padding(10.dp))
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
                                Text("How much € would you like to add?")
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
                                        bvm.user.value.credit += (input.value.text.toInt() * 100)
                                        bvm.updateUserBalance()
                                        openDialog.value = false
                                    }) {
                                        Text(text = "Confirm")
                                    }
                                    Spacer(modifier = Modifier.size(10.dp))
                                    Button(onClick = {
                                        openDialog.value = false
                                        input.value = TextFieldValue("")
                                    }) {
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