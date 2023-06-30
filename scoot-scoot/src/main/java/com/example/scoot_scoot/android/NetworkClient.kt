package com.example.scoot_scoot.android

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object NetworkClient {

    private val client: OkHttpClient

    init {

        client = OkHttpClient.Builder()
            .hostnameVerifier { _, _ -> true }
            .build()
    }



    fun makeRequest(): String? {
        val url = "http://10.0.2.2:8080/getScooter/1"
        val request = Request.Builder()
            .url(url)
            .build()

        try {
            val response: Response = client.newCall(request).execute()
            return if (response.isSuccessful) {
                response.body?.string()
            } else {
                // Handle the error response
                null
            }
        } catch (e: IOException) {
            // Handle the exception
            e.printStackTrace()
            return null
        }
    }

}