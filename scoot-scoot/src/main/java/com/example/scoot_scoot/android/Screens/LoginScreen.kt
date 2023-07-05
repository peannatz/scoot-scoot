package com.example.scoot_scoot.android.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scoot_scoot.android.Data.UserData
import com.example.scoot_scoot.android.Data.UserManager
import com.example.scoot_scoot.android.R
import com.example.scoot_scoot.android.Repository.UserRepository
import com.example.scoot_scoot.android.Screens.RegisterScreen.keyboardController
import com.example.scoot_scoot.android.ViewModels.LoginCallback
import com.example.scoot_scoot.android.ViewModels.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

object LoginScreen {

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun LoginScreen(navController: NavController, lvm: LoginViewModel = viewModel()) {
        val keyboardController = LocalSoftwareKeyboardController.current


        val loginCallback = remember {
            object : LoginCallback {
                override fun onLoginSuccess(user: UserData) {
                    MainScope().launch {
                        UserManager.saveUser(user)
                        if(!UserManager.getPermissionsStatus()){
                            navController.navigate(Screens.Permissions)
                        }else{
                            navController.navigate(Screens.Map)
                        }
                    }
                }

                override fun onLoginError(errorMessage: String) {
                    // Handle error
                }
            }
        }

        //TODO: filter whitespace in register and login form

        Box(modifier = Modifier.fillMaxSize()) {
            ClickableText(
                //TODO: Find a better font
                text = AnnotatedString("Sign me up good sir"),
                onClick = { navController.navigate(Screens.Register) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colors.onBackground
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_scooter),
                contentDescription = "",
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(width = 250.dp, height = 250.dp)
            )

            TextField(
                value = lvm.email.value,
                onValueChange = { lvm.email.value = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = { RegisterScreen.keyboardController?.hide() }),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = lvm.password.value,
                onValueChange = { lvm.password.value = it },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                label = { Text("Password") },
            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp),
                text = lvm.loginErrorMsg.value,
                fontSize = 14.sp,
                color = MaterialTheme.colors.secondary,
            )
            Button(
                onClick = {
                    lvm.viewModelScope.launch(Dispatchers.IO) {
                        lvm.checkLoginData(
                            loginCallback
                        )
                    }
                },
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(text = "Login", modifier = Modifier.padding(5.dp))

            }
        }
    }
}