package com.example.scoot_scoot.android.Repository

import com.example.scoot_scoot.android.Data.Location
import com.example.scoot_scoot.android.Data.RouteModel
import com.example.scoot_scoot.android.Network.GoogleClient
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleRepository {

    suspend fun getRoute(origin: LatLng, destination: String): RouteModel? {
        return withContext(Dispatchers.IO) {
            val destinationLatLng = getPlaceLatLng(destination)
            GoogleClient.request(origin, destinationLatLng)
        }
    }

    suspend fun getPlaceLatLng(id: String): LatLng {
        return withContext(Dispatchers.IO) {
            GoogleClient.requestPlaceInfo(id)!!.latLng!!
        }
    }

    suspend fun getAddressFromLatLng(location: Location): String {
        return withContext(Dispatchers.IO) {
            GoogleClient.requestAddressFromLatLng(location) ?: ""
        }
    }
}