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

    private val baseUrl = "http://10.0.2.2:8080/"

    fun getScooterById(id: Int): String? {
        val url = "${baseUrl}scooter/getScooter/$id"
        return getRequest(url)
    }

    fun addUser(userData: RegisterUser): Boolean {
        val url = "${baseUrl}user/add"

        val gson = Gson()
        val jsonPayload = gson.toJson(userData)

        return postRequest(url, jsonPayload)
    }

    private fun postRequest(url: String, body: String): Boolean{
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
            }else{
                println(response.message)
                return true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
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