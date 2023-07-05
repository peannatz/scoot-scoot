package com.example.scoot_scoot.android.Network

import android.content.ContentValues.TAG
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import com.example.scoot_scoot.android.BuildConfig
import com.example.scoot_scoot.android.Components.AutocompleteSearchBox.placesClient
import com.example.scoot_scoot.android.Data.Location
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


object GoogleClient {
    val gson = Gson()

    data class ComputeRoutesRequest(
        val origin: Origin,
        val destination: Destination,
        val travelMode: String,
        val routing_preference: String
    )

    data class ComputeRoutesResponse(
        val routes: List<Route>
    )

    data class Route(
        val distanceMeters: Int,
        val routeToken: String,
    )

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalRequest = chain.request()

            val modifiedHttpUrl = originalRequest.newBuilder()
                .addHeader("X-Goog-Api-Key", BuildConfig.MAPS_API_KEY)
                .build()


            chain.proceed(modifiedHttpUrl)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addNetworkInterceptor(loggingInterceptor)
        .build()

    val baseUrl = "https://routes.googleapis.com/directions/v2:computeRoutes"

    fun requestPlaceInfo(id: String): Place? {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(id, placeFields)

        var result: Place? = null
        try {
            result = Tasks.await(placesClient!!.fetchPlace(request)).place

        } catch (e: ExecutionException) {
            // Catch me
            println(e.message)
        } catch (e: TimeoutException) {
            println(e.message)

            // Catch me, only needed when a timeout is set
        } catch (e: InterruptedException) {
            println(e.message)
            // Catch me
        }

        if (result != null) {
            return result
        } else {
            return null
        }
    }


    fun request(origin: LatLng, destination: LatLng): Route? {
        val requestData = ComputeRoutesRequest(
            origin = Origin(Location(origin)),
            destination = Destination(Location(destination)),
            travelMode = "DRIVE",
            routing_preference = "TRAFFIC_AWARE"
        )


        val json = gson.toJson(requestData)
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url(baseUrl)
            .header("Content-Type", "application/json")
            .header(
                "X-Goog-FieldMask",
                "routes.distanceMeters,routes.route_token"
            )
            .post(requestBody)
            .build()

        try {
            val response: Response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                println("Unsuccessful response: ${response.code} ${response.message}")
                return null
            } else {
                println(response.body)
                val route = gson.fromJson(response.body.string(), ComputeRoutesResponse::class.java)
                return route.routes[0]
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }


    data class Origin(
        val location: Location
    )

    data class Destination(
        val location: Location
    )

    data class Location(
        val latLng: LatLng
    )
}