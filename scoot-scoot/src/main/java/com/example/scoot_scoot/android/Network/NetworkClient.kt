package com.example.scoot_scoot.android.Network

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException

abstract class NetworkClient {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .hostnameVerifier { _, _ -> true }
        .build()

    // Use deviceUrl, when connecting to the backend in the same wifi
    // Use emulatorUrl, when using the emulator to fetch from the backend

    // Use usbUrl, when connecting using only a usb connection.
    // Use adb reverse tcp:8080 tcp:8080 to forward the port to the device.

    protected val deviceUrl = "http://192.168.6.236:8080/"
    protected val usbUrl = "http://127.0.0.1:8080/"
    protected val emulatorUrl = "http://10.0.2.2:8080/"
    protected val baseUrl = usbUrl

    private fun chooseUrl():String {
        if (Build.PRODUCT.contains("sdk") || Build.MODEL.contains("emu")) {
            return emulatorUrl
        }else{
            return deviceUrl
        }
    }
    protected fun postRequest(url: String, body: String): String? {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = body.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                println("Unsuccessful response: ${response.code} ${response.message}")
            } else {
                return response.body.string()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    protected fun getRequest(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            return if (response.isSuccessful) {
                response.body.string()
            } else {
                println("Unsuccessful response: ${response.code} ${response.message}")
                return null
            }
        } catch (e: IOException) {
            // Handle the exception
            e.printStackTrace()
            return null
        }
    }

}