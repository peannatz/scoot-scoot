package com.example.scoot_scoot.android.ViewModels

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoot_scoot.android.Data.ScooterModel
import com.example.scoot_scoot.android.Repository.ScooterRepository
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel():ViewModel() {

    private val scooterRepository=ScooterRepository()
    var selectedScooter: MutableState<ScooterModel?> = mutableStateOf(null)

    var useLocation = mutableStateOf(true)
    lateinit var fusedLocationClient: FusedLocationProviderClient


    @OptIn(ExperimentalMaterialApi::class)
    var sheetState = ModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded })


    private val scooterList = MutableStateFlow(listOf<ScooterModel>())
    val scooters: StateFlow<List<ScooterModel>> get() = scooterList

    init{
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val fetchedData = scooterRepository.getAllScooters()
                scooterList.emit(fetchedData)
            }
        }
    }
}