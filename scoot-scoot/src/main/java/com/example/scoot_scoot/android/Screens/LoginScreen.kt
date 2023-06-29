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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scoot_scoot.android.R

object LoginScreen {
    @Composable
    fun LoginScreen(navController: NavController) {
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
            Image(
                painter = painterResource(id = R.drawable.ic_scooter),
                contentDescription = "",
                modifier = Modifier.size(width = 250.dp, height = 250.dp)
            )
            val email = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }

            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                label = { Text("Password") },
                //shape = RoundedCornerShape(0, 0, 20, 20)
            )
            Button(
                onClick = { },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(text = "Login")

            }
        }
    }
}