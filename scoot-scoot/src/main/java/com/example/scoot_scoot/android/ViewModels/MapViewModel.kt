package com.example.scoot_scoot.android.ViewModels

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoot_scoot.android.Data.HelpData
import com.example.scoot_scoot.android.Data.HelpDataModel
import com.example.scoot_scoot.android.Data.Location
import com.example.scoot_scoot.android.Data.ScooterModel
import com.example.scoot_scoot.android.Network.ScooterClient
import com.example.scoot_scoot.android.Repository.ScooterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel:ViewModel() {

    private val scooterRepository=ScooterRepository()

    var selectedScooter= ScooterModel(-1, "", 1, false, Location(0,0f,0f))

    fun UpdateSelectedScooter(scooter: ScooterModel){
        selectedScooter=scooter; }

    @OptIn(ExperimentalMaterialApi::class)
    var sheetState = ModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded })

    var updateScooterInfo=false;

    private val itemsList = MutableStateFlow(listOf<ScooterModel>())
    val items: StateFlow<List<ScooterModel>> get() = itemsList

    init{
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val fetchedData = scooterRepository.getAllScooters()
                itemsList.emit(fetchedData)
            }
        }
    }
}