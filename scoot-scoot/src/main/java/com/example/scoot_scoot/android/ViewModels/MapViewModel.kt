package com.example.scoot_scoot.android.ViewModels

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoot_scoot.android.Data.HelpData
import com.example.scoot_scoot.android.Data.HelpDataModel
import com.example.scoot_scoot.android.Data.Location
import com.example.scoot_scoot.android.Data.ScooterModel
import com.example.scoot_scoot.android.Network.ScooterClient
import com.example.scoot_scoot.android.Repository.ScooterRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel():ViewModel() {

    private val scooterRepository=ScooterRepository()

    var selectedScooter= ScooterModel(-1, "", 1, false, Location(0,0f,0f))
    var selectedScooter1: MutableState<ScooterModel?> = mutableStateOf(null)

    var useLocation = mutableStateOf(true)

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var cameraState: MutableState<CameraPositionState>

    fun UpdateSelectedScooter(scooter: ScooterModel){
        selectedScooter=scooter; }

    @OptIn(ExperimentalMaterialApi::class)
    var sheetState = ModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded })


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