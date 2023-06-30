package com.example.scoot_scoot.android

import com.example.scoot_scoot.android.Data.RegisterUser
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.IOException

object NetworkClient {

    private val client: OkHttpClient = OkHttpClient.Builder()
        .hostnameVerifier { _, _ -> true }
        .build()

    private val baseUrl = "http://10.0.2.2:8080"

    fun getScooterById(id: Int): String? {
        val url = "${baseUrl}/getScooter/$id"
        return getRequest(url)
    }

    fun addUser(userData: RegisterUser) {
        val url = "${baseUrl}/addUser"

        val gson = Gson()
        val jsonPayload = gson.toJson(userData)

        print(
            jsonPayload
        )
        postRequest(url, jsonPayload)
    }

    private fun postRequest(url: String, body: String) {
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
            }else{
                println(response.body)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getRequest(url: String): String? {
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