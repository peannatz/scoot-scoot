package com.example.scoot_scoot.android.ViewModels

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.ViewModel
import com.example.scoot_scoot.android.Scooter
import java.math.BigDecimal

@OptIn(ExperimentalMaterialApi::class)
class SharedViewModel : ViewModel() {
    var selectedScooter= Scooter(-1, "", BigDecimal(0.1), BigDecimal(0.1))

    fun UpdateSelectedScooter(scooter: Scooter){
        selectedScooter=scooter; }

    var sheetState = ModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded })

    var updateScooterInfo=false;
}
