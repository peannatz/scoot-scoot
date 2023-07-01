package com.example.scoot_scoot.android.Network

import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

abstract class NetworkClient {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .hostnameVerifier { _, _ -> true }
        .build()

    protected val baseUrl = "http://10.0.2.2:8080/"

    protected fun postRequest(url: String, body: String): Boolean {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = RequestBody.create(mediaType, body)
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                println("Unsuccessful response: ${response.code} ${response.message}")
                return false
            } else {
                println(response.message)
                return true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    protected fun getRequest(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            return if (response.isSuccessful) {
                response.body?.string()
            } else {
                println("Unsuccessful response: ${response.code} ${response.message}")
                null
            }
        } catch (e: IOException) {
            // Handle the exception
            e.printStackTrace()
            return null
        }
    }

}