package com.example.scoot_scoot.android.Data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object LastLocation {
    private val _sharedVariable = MutableStateFlow(Location(0.00, 0.00))
    var value: StateFlow<Location> = _sharedVariable

    fun updateSharedVariable(newLocation: Location) {
        _sharedVariable.value = newLocation
    }
}