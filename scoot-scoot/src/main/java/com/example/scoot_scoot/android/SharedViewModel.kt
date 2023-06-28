package com.example.scoot_scoot.android

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal

class SharedViewModel : ViewModel() {
    var selectedScooter= Scooter(-1, "", BigDecimal(0.1), BigDecimal(0.1))

    fun UpdateSelectedScooter(scooter: Scooter){
        selectedScooter=scooter; }
}

data class BottomSheet(
    val visible: Boolean = false
)
