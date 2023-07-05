package com.example.scoot_scoot.android.ViewModels

import android.location.Location
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoot_scoot.android.Data.AutoCompleteResult
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch

class AutocompleteViewModel() : ViewModel() {
    val locationAutofill = mutableStateListOf<AutoCompleteResult>()
    fun searchPlaces(query: String, placesClient: PlacesClient, lastLocation: Location) {
        if (query.length <= 3) {
            return
        }
        val autocompleteSessionToken = AutocompleteSessionToken.newInstance()

        val bias = RectangularBounds.newInstance(
            getCoordinate(
                lastLocation.latitude,
                lastLocation.longitude,
                -1000,
                -1000
            )!!,
            getCoordinate(
                lastLocation.latitude,
                lastLocation.longitude,
                1000,
                1000
            )!!
        )
        locationAutofill.clear()
        viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest
                .builder()
                .setLocationBias(bias)
                .setSessionToken(autocompleteSessionToken)
                .setQuery(query)
                .build()
            placesClient
                .findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    val predictionLimit = 3

                    locationAutofill += response.autocompletePredictions
                        .take(predictionLimit).map {
                            AutoCompleteResult(
                                it.getFullText(null).toString(),
                                it.placeId
                            )
                        }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    println(it.cause)
                    println(it.message)
                }

        }
    }

    fun getCoordinate(lat0: Double, lng0: Double, dy: Long, dx: Long): LatLng? {
        val lat = lat0 + 180 / Math.PI * (dy / 6378137)
        val lng = lng0 + 180 / Math.PI * (dx / 6378137) / Math.cos(lat0)
        return LatLng(lat, lng)
    }


}