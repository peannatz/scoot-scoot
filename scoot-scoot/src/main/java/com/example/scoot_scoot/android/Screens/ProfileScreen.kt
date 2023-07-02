package com.example.scoot_scoot.android.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scoot_scoot.android.Components.ProfileBottomBar.ProfileBottomBar
import com.example.scoot_scoot.android.Data.UserDataModel
import com.example.scoot_scoot.android.Network.UserClient
import com.example.scoot_scoot.android.R
import com.example.scoot_scoot.android.ViewModels.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object ProfileScreen {
    @Composable
    fun ProfileScreen(navController: NavController, pvm: ProfileViewModel = viewModel()) {
        if (pvm.userFetched.value) {
            Scaffold(
                bottomBar = {
                    BottomAppBar(modifier = Modifier.height(70.dp)) { ProfileBottomBar(navController) }
                }
            ) { paddingValues ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(paddingValues)

                ) {
                    UserDataEntry(data = pvm.surnameData, pvm)
                    UserDataEntry(data = pvm.nameData, pvm)
                    UserDataEntry(data = pvm.emailData, pvm)
                    UserDataEntry(data = pvm.birthdateData, pvm)
                }
            }
        }
    }

    //TODO: Errormessage on invalid input

    @Composable
    fun EditIcon(inEditMode: MutableState<Boolean>, onEditModeExit: (Boolean) -> Unit) {
        val focusManager = LocalFocusManager.current

        if (inEditMode.value) {
            Row(Modifier.padding(end = 20.dp)) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Editable Field",
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {
                            inEditMode.value = false;
                            focusManager.clearFocus()
                            onEditModeExit(false)
                        }
                )
                Spacer(modifier = Modifier.size(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.save),
                    contentDescription = "Editable Field",
                    modifier = Modifier
                        .size(60.dp)
                        .clickable {
                            inEditMode.value = false;
                            focusManager.clearFocus()
                            onEditModeExit(true)
                        }
                )
            }
        } else {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editable Field",
                modifier = Modifier
                    .size(60.dp)
                    .clickable {
                        inEditMode.value = true
                    }
            )
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun UserDataEntry(
        data: UserDataModel,
        pvm: ProfileViewModel
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val inEditMode = remember { mutableStateOf(false) }
        val preEditState = remember { mutableStateOf("") }

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
                value = data.data.value,
                modifier=Modifier.onFocusChanged { if(it.hasFocus){} },
                onValueChange = {
                    if (preEditState.value == "") {
                        preEditState.value = data.data.value
                    }
                    data.data.value = it
                    data.validation
                },
                singleLine = true,
                enabled = inEditMode.value,
                interactionSource = interactionSource,
                textStyle = TextStyle(
                    fontSize = 40.sp,
                    color = MaterialTheme.colors.onPrimary,
                    textDecoration = TextDecoration.None
                ),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        TextFieldDefaults.TextFieldDecorationBox(
                            value = data.data.value,
                            label = { Text(text = data.type, style = TextStyle(fontSize = 20.sp)) },
                            innerTextField = innerTextField,
                            singleLine = true,
                            enabled = inEditMode.value,
                            visualTransformation = VisualTransformation.None,
                            trailingIcon = {
                                if (data.type != "Birthdate") EditIcon(
                                    inEditMode = inEditMode,
                                    onEditModeExit = { booleanValue ->
                                        data.edited.value = true
                                        onEditModeExit(booleanValue,data, pvm, preEditState)
                                    }
                                )
                            },
                            interactionSource = interactionSource,
                        )
                    }
                }
            )

        }
    }

    private fun onEditModeExit(save: Boolean, data: UserDataModel, pvm: ProfileViewModel, preEdit:MutableState<String>) {
        if (save) {
            CoroutineScope(Dispatchers.IO).launch {
                pvm.handleInputChange()
            }
        }else{
            data.data.value=preEdit.value
        }
    }
}