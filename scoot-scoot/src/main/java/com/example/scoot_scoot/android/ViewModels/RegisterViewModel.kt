package com.example.scoot_scoot.android.ViewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale

class RegisterViewModel : UserDataViewModel() {

    var isBirthdateInvalid: MutableState<Boolean> = mutableStateOf(false)
    var birthdateErrMsg: MutableState<String> = mutableStateOf("")

    var termsAndConditionsAccepted: MutableState<Boolean> = mutableStateOf(false)
    var isEnabledRegisterButton: MutableState<Boolean> = mutableStateOf(false)

    override fun handleInputChange() {
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
                && termsAndConditionsAccepted.value
    }

    fun validateBirthdate() {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("ddMMyyyy")
        val birthday = LocalDate.parse(birthdate.value, formatter)
        if (Period.between(birthday, today).years < 18) {
            isBirthdateInvalid.value = true
            birthdateErrMsg.value = "You must be over 18"
        } else {
            isBirthdateInvalid.value = false
            birthdateErrMsg.value = ""
        }
        handleInputChange()
    }

    fun termsAndConditionsChecked() {
        handleInputChange()
    }

    suspend fun register(registerCallback: RegisterCallback) {
        val birthdate = formattedDateString()
        userData.name = name.value
        userData.surname = surname.value
        userData.birthdate = birthdate
        userData.email = email.value
        userData.password = password.value
        Log.d("name", name.value)
        Log.d("surname", surname.value)
        Log.d("Birthdate", birthdate)
        Log.d("email", email.value)
        Log.d("password", password.value)
        Log.d("confirmPassword", confirmPassword.value)
        Log.d("user", userData.toString())

        if (userRepository.registerUser(userData)) {
            registerCallback.onRegisterSuccess()
        } else {
            registerCallback.onRegisterError("")
        }
    }

    private fun formattedDateString(): String {
        val dateParser = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        val birthdateAsDate = dateParser.parse(birthdate.value)
        val dateFormater = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedBirthdate = birthdateAsDate?.let { dateFormater.format(it) }
        return formattedBirthdate as String
    }
}

interface RegisterCallback {
    fun onRegisterSuccess()
    fun onRegisterError(errorMessage: String)
}
