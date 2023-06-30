package com.example.scoot_scoot.android.ViewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.scoot_scoot.android.Data.RegisterUser
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class RegisterViewModel : ViewModel() {

    var regUser: RegisterUser = RegisterUser()

    var name: MutableState<String> = mutableStateOf(regUser.name)
    var isNameInvalid: MutableState<Boolean> = mutableStateOf(false)
    var nameErrMsg: MutableState<String> = mutableStateOf("")

    var surname: MutableState<String> = mutableStateOf(regUser.surname)
    var isSurnameInvalid: MutableState<Boolean> = mutableStateOf(false)
    var surnameErrMsg: MutableState<String> = mutableStateOf("")

    var birthdate: MutableState<Date> = mutableStateOf(regUser.birthdate)
    var isBirthdateInvalid: MutableState<Boolean> = mutableStateOf(false)
    var birthdateErrMsg: MutableState<String> = mutableStateOf("")

    var email: MutableState<String> = mutableStateOf(regUser.email)
    var isEmailInvalid: MutableState<Boolean> = mutableStateOf(false)
    var emailErrMsg: MutableState<String> = mutableStateOf("")

    var password: MutableState<String> = mutableStateOf("")
    var isPasswordInvalid: MutableState<Boolean> = mutableStateOf(false)
    var passwordErrMsg: MutableState<String> = mutableStateOf("")

    var confirmPassword: MutableState<String> = mutableStateOf("")
    var isConfirmPasswordInvalid: MutableState<Boolean> = mutableStateOf(false)
    var confPasswordErrMsg: MutableState<String> = mutableStateOf("")

    var isEnabledRegisterButton: MutableState<Boolean> = mutableStateOf(false)

    private fun shouldEnabledRegisterButton() {
        isEnabledRegisterButton.value = nameErrMsg.value.isEmpty()
                && surnameErrMsg.value.isEmpty()
                && emailErrMsg.value.isEmpty()
                && passwordErrMsg.value.isEmpty()
                && confPasswordErrMsg.value.isEmpty()
                && name.value.isNotEmpty()
                && surname.value.isNotEmpty()
                && email.value.isNotEmpty()
                && password.value.isNotEmpty()
                && confirmPassword.value.isNotEmpty()
    }

    fun validateName() {
        if (name.value.trim().isEmpty()) {
            isNameInvalid.value = true
            nameErrMsg.value = "Name can't be empty"
        } else {
            isNameInvalid.value = false
            nameErrMsg.value = ""
        }
        shouldEnabledRegisterButton()
    }

    fun validateSurname() {
        if (surname.value.trim().isEmpty()) {
            isSurnameInvalid.value = true
            surnameErrMsg.value = "Surname can't be empty"
        } else {
            isSurnameInvalid.value = false
            surnameErrMsg.value = ""
        }
        shouldEnabledRegisterButton()
    }

    //TODO: kann man dates auch besser vergleichen?
    fun validateBirthday() {
        val today = LocalDate.now()
        val formattedBirthdate= birthdate.value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        if (Period.between(formattedBirthdate, today).years < 18) {
            isBirthdateInvalid.value = true
            birthdateErrMsg.value = "You must be over 18"
        } else {
            isBirthdateInvalid.value = false
            birthdateErrMsg.value = ""
        }
        shouldEnabledRegisterButton()
    }

    fun validateEmail() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            isEmailInvalid.value = true
            emailErrMsg.value = "Input proper email id"
        } else {
            isEmailInvalid.value = false
            emailErrMsg.value = ""
        }
        shouldEnabledRegisterButton()
    }

    fun validatePassword() {
        if (password.value != "123") {
            isPasswordInvalid.value = true
            passwordErrMsg.value = "Password should be 123"
        } else {
            isPasswordInvalid.value = false
            passwordErrMsg.value = ""
        }
        shouldEnabledRegisterButton()
    }

    fun validateConfirmPassword() {
        if (password.value != confirmPassword.value) {
            isConfirmPasswordInvalid.value = true
            confPasswordErrMsg.value = "Password did not match"
        } else {
            isConfirmPasswordInvalid.value = false
            confPasswordErrMsg.value = ""
        }
        shouldEnabledRegisterButton()
    }

    fun register() {
        regUser.name = name.value
        regUser.surname = surname.value
        regUser.birthdate = birthdate.value
        regUser.email = email.value
        //TODO: add password
        //regUser.password = password.value
        Log.d("Karthik name", name.value)
        Log.d("Karthik surname", surname.value)
        Log.d("Karthik Birthday", birthdate.value.toString())
        Log.d("Karthik email", email.value)
        Log.d("Karthik password", password.value)
        Log.d("Karthik", regUser.toString())
    }

}