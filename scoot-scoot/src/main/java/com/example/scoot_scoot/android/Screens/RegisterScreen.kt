package com.example.scoot_scoot.android.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scoot_scoot.android.NetworkClient
import com.example.scoot_scoot.android.R
import com.example.scoot_scoot.android.ViewModels.RegisterViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object RegisterScreen {
    @Composable
    fun RegisterScreen(navController: NavController, rvm: RegisterViewModel = viewModel()) {

        var fields = mapOf<String, MutableState<TextFieldValue>>()


        val termsAndConditions = remember { mutableStateOf(false) }
        val popup = remember { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_scooter),
                contentDescription = "",
                modifier = Modifier.size(width = 250.dp, height = 250.dp)
            )
            Text(
                text = AnnotatedString("Are you ready for the sound of Scooter?"),
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(bottom = 40.dp)
            )
            NameField(rvm)
            SurnameField(rvm)
            EmailField(rvm)
            BirthdateField1(rvm)
            //BirthdateField(rvm)
            PasswordField(rvm)
            PasswordConfirmationField(rvm)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = termsAndConditions.value,
                    onCheckedChange = { termsAndConditions.value = it })
                Text(
                    text = "I agree to the ",
                    style = TextStyle(
                        fontSize = 15.sp,
                    )
                )
                ClickableText(
                    text = AnnotatedString("terms and conditions"),
                    onClick = { popup.value = !popup.value },
                    style = TextStyle(
                        color = MaterialTheme.colors.secondary,
                        fontSize = 15.sp,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
            Button(onClick = {
                Thread {
                    rvm.register()
                    NetworkClient.addUser(rvm.regUser)
                    val test = NetworkClient.getScooterById(1)
                    println(test)
                }.start()
            }, enabled = rvm.isEnabledRegisterButton.value) {
                Text(text = "Register")
            }
        }
        if (popup.value) {
            Popup.Show(popup)
        }

    }

    @Composable
    fun NameField(rvm: RegisterViewModel) {
        Box {
            TextField(
                value = rvm.name.value,
                onValueChange = {
                    rvm.name.value = it
                    rvm.validateName()
                },
                label = { Text(text = "Name") },
                singleLine = true
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.BottomCenter),
                text = rvm.nameErrMsg.value,
                fontSize = 14.sp,
                color = MaterialTheme.colors.secondary,
            )
        }
    }

    @Composable
    fun SurnameField(rvm: RegisterViewModel) {
        Box {
            TextField(
                value = rvm.surname.value,
                onValueChange = {
                    rvm.surname.value = it
                    rvm.validateSurname()
                },
                label = { Text(text = "Surname") },
                singleLine = true,
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.BottomCenter),
                text = rvm.surnameErrMsg.value,
                fontSize = 14.sp,
                color = MaterialTheme.colors.error
            )
        }
    }

    @Composable
    fun EmailField(rvm: RegisterViewModel) {
        Box {
            TextField(
                value = rvm.email.value,
                onValueChange = {
                    rvm.email.value = it
                    rvm.validateEmail()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                label = { Text(text = "Email") },
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.BottomCenter),
                text = rvm.emailErrMsg.value,
                fontSize = 14.sp,
                color = MaterialTheme.colors.error
            )
        }

    }

    @Composable
    fun PasswordField(rvm: RegisterViewModel) {
        Box {
            TextField(
                value = rvm.password.value,
                onValueChange = {
                    rvm.password.value = it
                    rvm.validatePassword()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                label = { Text(text = "Password") },
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.BottomCenter),
                text = rvm.passwordErrMsg.value,
                fontSize = 14.sp,
                color = MaterialTheme.colors.error
            )
        }

    }

    @Composable
    fun PasswordConfirmationField(rvm: RegisterViewModel) {
        Box {
            TextField(
                value = rvm.confirmPassword.value,
                onValueChange = {
                    rvm.confirmPassword.value = it
                    rvm.validateConfirmPassword()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                label = { Text(text = "Confirm Password") },
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.BottomCenter),
                text = rvm.confPasswordErrMsg.value,
                fontSize = 14.sp,
                color = MaterialTheme.colors.error
            )
        }

    }

    @Composable
    fun BirthdateField(rvm: RegisterViewModel) {
        val format = SimpleDateFormat("ddMMyyyy", Locale.ENGLISH)
        Box {
            TextField(
                value = format.format(rvm.birthdate.value).toString(),
                onValueChange = {
                    rvm.birthdate.value = format.parse(it) as Date
                    if(rvm.birthdate.value.toString().length==8){
                        rvm.validateBirthday()
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = DateTransformation(),
                singleLine = true,
                label = { Text(text = "DD.MM.YYYY") },
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.BottomCenter),
                text = rvm.birthdateErrMsg.value,
                fontSize = 14.sp,
                color = MaterialTheme.colors.error
            )
        }
    }

    @Composable
    fun BirthdateField1(rvm: RegisterViewModel) {
        Box {
            val inputText = rvm.birthdate.value?.toInputString() ?: "DDMMYYYY"
            TextField(
                value = inputText,
                onValueChange = { newValue ->
                    val filteredValue = newValue.filter { it.isDigit() }
                    rvm.birthdate.value = filteredValue.toDateOrNull()!!
                    if (filteredValue.length == 8) {
                        //rvm.validateBirthday()
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                label = { Text(text = "DD.MM.YYYY") },
            )
            Text(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.BottomCenter),
                text = rvm.birthdateErrMsg.value,
                fontSize = 14.sp,
                color = MaterialTheme.colors.error
            )
        }
    }

    fun String.toDateOrNull(): Date? {
        return try {
            SimpleDateFormat("ddMMyyyy", Locale.ENGLISH).parse(this)
        } catch (e: ParseException) {
            null
        }
    }

    fun Date.toInputString(): String {
        val format = SimpleDateFormat("ddMMyyyy", Locale.ENGLISH)
        return format.format(this)
    }


//TODO: move this, keep placeholder and only replace one char at a time

    class DateTransformation() : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            return dateFilter(text)
        }
    }

    fun dateFilter(text: AnnotatedString): TransformedText {

        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 2 == 1 && i < 4) out += "."
        }

        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                if (offset <= 8) return offset + 2
                return 10
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                if (offset <= 10) return offset - 2
                return 8
            }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }

}
