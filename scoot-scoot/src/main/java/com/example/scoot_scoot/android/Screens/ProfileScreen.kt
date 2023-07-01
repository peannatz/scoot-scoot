package com.example.scoot_scoot.android.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.BottomAppBar
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scoot_scoot.android.Components.ProfileBottomBar.ProfileBottomBar
import com.example.scoot_scoot.android.R

object ProfileScreen {
    @Composable
    fun ProfileScreen(navController: NavController) {
        //TODO: Add clickability and option to edit and move to own component
        Scaffold(
            bottomBar = {
                BottomAppBar(modifier = Modifier.height(70.dp)){ ProfileBottomBar(navController)}
            }
        ) { paddingValues ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(paddingValues)

            ) {
                UserDataEntry(type = "Surname", data = "Uwe", editable = true)
                UserDataEntry(type = "Name", data = "Uwington", editable = true)
                UserDataEntry(type = "Email", data = "uwe@uwe.de", editable = true)
                UserDataEntry(type = "Birthday", data = "01.01.1990", editable = false)
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun UserDataEntry(type: String, data: String, editable: Boolean) {
        val interactionSource = remember { MutableInteractionSource() }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(vertical = 2.dp)
                .background(
                    MaterialTheme.colors.primary
                )
        ) {
            BasicTextField(
                value = data,
                onValueChange = { it },
                singleLine = true,
                interactionSource = interactionSource,
                readOnly = !editable,
                textStyle = TextStyle(
                    fontSize = 40.sp, color = MaterialTheme.colors.onPrimary
                ),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        TextFieldDefaults.TextFieldDecorationBox(
                            value = data,
                            label = { Text(text = type, style = TextStyle(fontSize = 20.sp)) },
                            innerTextField = innerTextField,
                            singleLine = true,
                            enabled = true,
                            visualTransformation = VisualTransformation.None,
                            trailingIcon = {
                                if (editable) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowRight,
                                        contentDescription = "Editable Field",
                                        modifier = Modifier.size(80.dp)
                                    )
                                }
                            },
                            interactionSource = interactionSource,
                        )
                    }
                },
            )
        }
    }
}