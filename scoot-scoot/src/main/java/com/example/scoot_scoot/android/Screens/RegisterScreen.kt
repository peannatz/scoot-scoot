package com.example.scoot_scoot.android.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.example.scoot_scoot.android.R


@UnstableApi object RegisterScreen {
    @Composable
    fun RegisterScreen(navController: NavController) {
        val name = remember { mutableStateOf(TextFieldValue()) }
        val surname = remember { mutableStateOf(TextFieldValue()) }
        val email = remember { mutableStateOf(TextFieldValue()) }
        val birthdate = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }
        val passwordConfirmation = remember { mutableStateOf(TextFieldValue()) }
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
            TextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text(text = "Name") })
            TextField(
                value = surname.value,
                onValueChange = { surname.value = it },
                label = { Text(text = "Surname") })
            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            TextField(
                value = birthdate.value,
                onValueChange = { birthdate.value = it },
                label = { Text(text = "Birthday") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = DateTransformation()
            )
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(text = "Password") })
            TextField(
                value = passwordConfirmation.value,
                onValueChange = { passwordConfirmation.value = it },
                label = { Text(text = "Confirm Password") })
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
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Register")
            }
        }
        if (popup.value) {
            Popup.Show(popup)
        }
    }
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
