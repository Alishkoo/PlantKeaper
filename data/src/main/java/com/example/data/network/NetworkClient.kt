package com.example.data.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException
import java.util.concurrent.TimeUnit


class NetworkClient {

    private val client: OkHttpClient

    init {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


        client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }


    fun get(url: String): String? {
        try {

            val request = Request.Builder()
                .url(url)
                .get()
                .build()


            val response: Response = client.newCall(request).execute()


            if (!response.isSuccessful) {
                println("Ошибка запроса: ${response.code} - ${response.message}")
                return null
            }


            return response.body?.string()
        } catch (e: IOException) {
            println("Ошибка сети: ${e.message}")
            return null
        }
    }
}