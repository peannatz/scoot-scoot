package com.example.scoot_scoot.android.Repository

import com.example.scoot_scoot.android.Network.GoogleClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleRepository {

    suspend fun getRoute(origin: LatLng, destination: String): GoogleClient.Route? {
        return withContext(Dispatchers.IO) {
            val destinationLatLng = getPlaceLatLng(destination)
            if (destinationLatLng != null) {
                GoogleClient.request(origin, destinationLatLng)
            } else {
                null
            }
        }
    }

    suspend fun getPlaceLatLng(id: String): LatLng {
        return withContext(Dispatchers.IO) {
            GoogleClient.requestPlaceInfo(id)!!.latLng!!
        }
    }
}