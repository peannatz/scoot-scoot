package com.example.scoot_scoot.android

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal

class SharedViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BottomSheet())
    val uiState: StateFlow<BottomSheet> = _uiState.asStateFlow()
    var selectedScooter:Scooter= Scooter(-1,"", BigDecimal(1.0), BigDecimal(1.0))


    fun UpdateSelectedScooter(scooter: Scooter){
        selectedScooter=scooter;
        _uiState.value = BottomSheet(true)}
}

data class BottomSheet(
    val visible: Boolean = false
)
