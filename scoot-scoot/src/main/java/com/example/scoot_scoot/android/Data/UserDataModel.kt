package com.example.scoot_scoot.android.Data

import androidx.compose.runtime.MutableState

data class UserDataModel(
    val type: String,
    val data: MutableState<String>,
    val edited: MutableState<Boolean>,
    val validation: () -> Unit
)
