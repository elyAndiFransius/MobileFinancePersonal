package com.example.personalfinancemobile.app.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
        private const val BASE_URL = "http://192.168.100.48:8000/api/" // untuk emulator Android akses localhost

        val instance: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

}