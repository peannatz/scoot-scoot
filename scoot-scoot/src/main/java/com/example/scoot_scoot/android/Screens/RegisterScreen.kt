package com.example.scoot_scoot.android.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.scoot_scoot.android.Data.UserData
import com.example.scoot_scoot.android.Data.UserManager
import com.example.scoot_scoot.android.R
import com.example.scoot_scoot.android.ViewModels.RegisterCallback
import com.example.scoot_scoot.android.ViewModels.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.math.min

@OptIn(ExperimentalComposeUiApi::class)
object RegisterScreen {

    var keyboardController: SoftwareKeyboardController? = null

    @Composable
    fun RegisterScreen(navController: NavController, rvm: RegisterViewModel = viewModel()) {

        keyboardController = LocalSoftwareKeyboardController.current

        val registerCallback = remember {
            object : RegisterCallback {
                override fun onRegisterSuccess(user: UserData) {
                    MainScope().launch {
                        UserManager.saveUser(user)
                        if (!UserManager.getPermissionsStatus()) {
                            navController.navigate(Screens.Permissions)
                        } else {
                            navController.navigate(Screens.Map)
                        }
                    }
                }

                override fun onRegisterError(errorMessage: String) {
                    println("Error: $errorMessage")
                }
            }
        }

        val popupVisible = remember { mutableStateOf(false) }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_scooter),
                contentDescription = "",
                tint = MaterialTheme.colors.secondary,
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
            BirthdateField(rvm)
            PasswordField(rvm)
            PasswordConfirmationField(rvm)
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rvm.termsAndConditionsAccepted.value,
                    onCheckedChange = {
                        rvm.termsAndConditionsAccepted.value = it
                        rvm.termsAndConditionsChecked()
                    })
                Text(
                    text = "I agree to the ",
                    style = TextStyle(
                        fontSize = 15.sp,
                    )
                )
                ClickableText(
                    text = AnnotatedString("terms and conditions"),
                    onClick = { popupVisible.value = !popupVisible.value },
                    style = TextStyle(
                        color = MaterialTheme.colors.secondary,
                        fontSize = 15.sp,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
            Button(onClick = {
                rvm.viewModelScope.launch(Dispatchers.IO) {
                    rvm.register(registerCallback)
                }
            }, enabled = rvm.isEnabledRegisterButton.value) {
                Text(text = "Register")
            }
            ClickableText(
                //TODO: Find a better font
                text = AnnotatedString("Please no I want to sign up"),
                onClick = { navController.navigate(Screens.Login) },
                modifier = Modifier
                    .padding(20.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colors.onBackground
                )
            )
        }
        if (popupVisible.value) {
            Popup.Show(popupVisible)
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
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }),
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
    fun BirthdateField(rvm: RegisterViewModel) {
        Box {
            TextField(
                value = rvm.birthdate.value,
                onValueChange = { newValue ->
                    if (newValue.length <= 8) {
                        rvm.birthdate.value = validateOnInput(newValue)
                    }
                    if (rvm.birthdate.value.length == 8) {
                        rvm.validateBirthdate()
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }),
                visualTransformation = DateTransformation(),
                singleLine = true,
                label = { Text(text = "Birthdate") },
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
    fun EmailField(rvm: RegisterViewModel) {
        Box {
            TextField(
                value = rvm.email.value,
                onValueChange = {
                    rvm.email.value = it
                    rvm.validateEmail()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }),
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
                    if (rvm.confirmPassword.value.isNotEmpty()) rvm.validateConfirmPassword()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }),
                visualTransformation = PasswordVisualTransformation(),
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
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }),
                visualTransformation = PasswordVisualTransformation(),
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

    private fun validateOnInput(input: String): String {
        val validatedInput = StringBuilder(
            when (input.length) {
                1 -> {
                    val dayPart = input.take(1).toIntOrNull()?.coerceIn(0, 3) ?: 0
                    dayPart.toString()
                }

                2 -> {
                    val dayPart = input.take(2).toIntOrNull()?.coerceIn(1, 31) ?: 31
                    if (dayPart < 10) {
                        dayPart.toString().padStart(2, '0')
                    } else {
                        dayPart.toString()
                    }
                }

                3 -> {
                    val dayPart = input.take(2)
                    val monthPart = input.substring(2, 3).toIntOrNull()?.coerceIn(0, 1) ?: 0
                    dayPart + monthPart.toString()
                }

                4 -> {
                    val dayPart = input.take(2)
                    val monthPart = input.substring(2, 4).toIntOrNull()?.coerceIn(1, 12) ?: 12
                    if (monthPart < 10) {
                        dayPart + (monthPart.toString().padStart(2, '0'))
                    } else {
                        dayPart + monthPart.toString()
                    }
                }

                5 -> {
                    val dayAndMonthPart = input.take(4)
                    val yearPart = input.substring(4, 5).toIntOrNull()?.coerceIn(1, 2) ?: 1
                    dayAndMonthPart + yearPart.toString()
                }

                6 -> {
                    val dayAndMonthPart = input.take(4)
                    var yearPart = input.substring(4, 6).toIntOrNull()?.coerceIn(19, 20) ?: 19
                    if (yearPart == 19 && input.substring(4, 5) == "2") yearPart = 20
                    dayAndMonthPart + yearPart.toString()
                }

                8 -> {
                    val dayAndMonthPart = input.take(4)
                    val yearPart = input.substring(4).toIntOrNull() ?: 2021
                    dayAndMonthPart + yearPart.toString()
                }

                else -> input
            }
        )

        return validatedInput.toString()
    }

    class DateTransformation : VisualTransformation {
        override fun filter(text: AnnotatedString): TransformedText {
            val trimmed = text.text.take(8)
            val transformedText = buildAnnotatedString {
                when (trimmed.length) {
                    0 -> append("DD.MM.YYYY")
                    1 -> append("${text}D.MM.YYYY")
                    2 -> append("${text}.MM.YYYY")
                    3 -> append("${text.substring(0, 2)}.${text.substring(2, 3)}M.YYYY")
                    4 -> append("${text.substring(0, 2)}.${text.substring(2, 4)}.YYYY")
                    5 -> append(
                        "${text.substring(0, 2)}.${text.substring(2, 4)}.${
                            text.substring(
                                4,
                                5
                            )
                        }YYY"
                    )

                    6 -> append(
                        "${text.substring(0, 2)}.${text.substring(2, 4)}.${
                            text.substring(
                                4,
                                6
                            )
                        }YY"
                    )

                    7 -> append(
                        "${text.substring(0, 2)}.${text.substring(2, 4)}.${
                            text.substring(
                                4,
                                7
                            )
                        }Y"
                    )

                    8 -> append(
                        "${text.substring(0, 2)}.${text.substring(2, 4)}.${
                            text.substring(
                                4,
                                8
                            )
                        }"
                    )

                    else -> append("")
                }
            }

            val offsetTranslator = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (offset >= 4) return offset + 2
                    if (offset >= 2) return offset + 1
                    return offset
                }

                override fun transformedToOriginal(offset: Int): Int {
                    val originalOffset = min(offset, text.length)
                    val transformedOffset =
                        transformedText.text.substring(0, originalOffset).count { it != '.' }
                    return min(transformedOffset, text.length)

                }
            }

            return TransformedText(transformedText, offsetTranslator)
        }
    }

}