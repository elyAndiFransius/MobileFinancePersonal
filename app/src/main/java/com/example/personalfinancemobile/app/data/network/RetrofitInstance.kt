package com.example.personalfinancemobile.app.data.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
        private const val BASE_URL = "http://192.168.100.48:8000/api/" // untuk emulator Android akses localhost

    fun getInstance(context: Context): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor(context)) //pasang interceptor token
            .build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}